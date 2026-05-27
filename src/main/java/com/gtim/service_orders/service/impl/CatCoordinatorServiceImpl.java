package com.gtim.service_orders.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gtim.service_orders.dao.CatResourceDAO;
import com.gtim.service_orders.dto.CatCoordinatorDTO;
import com.gtim.service_orders.dto.CoordinatorRequestDTO;
import com.gtim.service_orders.dto.CoordinadoresDTO;
import com.gtim.service_orders.dto.ResourceDeleteDTO;
import com.gtim.service_orders.dto.ProyectosColaboradorDTO;
import com.gtim.service_orders.dto.ColaboradoresCoordinadorDTO;
import com.gtim.service_orders.entity.CatArea;
import com.gtim.service_orders.entity.CatCoordinator;
import com.gtim.service_orders.mapper.CatCoordinatorMapper;
import com.gtim.service_orders.repository.CatCoordinatorRepository;
import com.gtim.service_orders.repository.CatAreaRepository;
import com.gtim.service_orders.repository.CatResourceRepository;
import com.gtim.service_orders.repository.ResourceAssignmentRepository;
import com.gtim.service_orders.service.CatCoordinatorService;
import com.gtim.service_orders.notification.EmailService;
import com.gtim.service_orders.notification.ExcelService;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;

import com.gtim.service_orders.constants.ValidatorUtils;
import com.gtim.service_orders.dto.ResultadosCargaMasivaDTO;
import com.gtim.service_orders.entity.CatResource;
import com.gtim.service_orders.exception.BusinessException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class CatCoordinatorServiceImpl implements CatCoordinatorService {

    private final CatCoordinatorRepository repository;
    private final CatAreaRepository areaRepository;
    private final CatResourceRepository resourceRepository;
    private final ResourceAssignmentRepository assignRepository;
    private final CatCoordinatorMapper mapper;
    private final ExcelService excel;
    private final EmailService email;

    @Autowired
    private CatResourceDAO resourceDAO;

    @Override
    public List<CatCoordinatorDTO> getActiveCoordinators() {
        Sort sort = Sort.by(Sort.Direction.ASC, "name");
        return repository.findByActiveTrue(sort)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CoordinatorRequestDTO create(
            CoordinatorRequestDTO request,
            String user
    ) {
        if (!this.existeDuplicado(request.getEmail(), user, 0)) {

            Long idCoordinator = null;
            CatCoordinator coordinatorInactive = this.reactivarCoordinador(request.getEmail());

            if (coordinatorInactive != null) {
                idCoordinator = coordinatorInactive.getId();
            }

            CatArea area = areaRepository.findById(request.getEngineeringId())
                    .orElseThrow(() -> new IllegalArgumentException("Area no encontrado"));

            CatCoordinator newCoordinator = CatCoordinator.builder()
                    .id(idCoordinator)
                    .name(request.getName())
                    .email(request.getEmail())
                    .engineering(area)
                    .phone(request.getPhone())
                    .gtimRole("Coordinador de área")
                    .managerName(null)
                    .managerEmail(null)
                    .status("Activo")
                    .createdBy(user)
                    .createdAt(LocalDateTime.now())
                    .active(true)
                    .build();

            CatCoordinator saved = repository.save(newCoordinator);

            return CoordinatorRequestDTO.builder()
                    .id(saved.getId())
                    .name(saved.getName())
                    .email(saved.getEmail())
                    .engineeringId(request.getEngineeringId())
                    .phone(saved.getPhone())
                    .build();
        } else {
            throw new BusinessException("Ya existe un coordinador con ese correo");
        }
    }

    @Override
    public CoordinatorRequestDTO update(
            CoordinatorRequestDTO request,
            String user
    ) {
        if (!Objects.equals(request.getId(), 0L)) {
            CatCoordinator coordinator = repository.findById(request.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Coordinador no existe"));

            CatArea area = areaRepository.findById(request.getEngineeringId())
                    .orElseThrow(() -> new IllegalArgumentException("Area no encontrado"));

            CatCoordinator updCoordinator = CatCoordinator.builder()
                    .id(coordinator.getId())
                    .name(request.getName())
                    .email(request.getEmail())
                    .engineering(area)
                    .phone(request.getPhone())
                    .gtimRole("Coordinador de área")
                    .managerName(null)
                    .managerEmail(null)
                    .status("Activo")
                    .createdBy(coordinator.getCreatedBy())
                    .createdAt(coordinator.getCreatedAt())
                    .updatedBy(user)
                    .updatedAt(LocalDateTime.now())
                    .active(true)
                    .build();

            CatCoordinator saved = repository.save(updCoordinator);

            return CoordinatorRequestDTO.builder()
                    .id(saved.getId())
                    .name(saved.getName())
                    .email(saved.getEmail())
                    .engineeringId(request.getEngineeringId())
                    .phone(saved.getPhone())
                    .build();
        } else {
            return this.create(request, user);
        }
    }

    @Override
    public ByteArrayInputStream descargarPlantilla() {
        InputStream resource = null;
        try {
            Sort sort = Sort.by(Sort.Direction.ASC, "description");
            List<CatArea> areas = areaRepository.findByActiveTrue(sort);

            resource = new ClassPathResource("plantilla_coordinadores.xlsx").getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            XSSFWorkbook workbook = new XSSFWorkbook(resource);
            XSSFSheet sheet = workbook.getSheetAt(1);
            int rowTable = 1;
            XSSFCell cell;

            XSSFFont fontHeader = workbook.createFont();
            fontHeader.setColor(IndexedColors.WHITE.getIndex());
            fontHeader.setBold(true);
            XSSFColor customColor = new XSSFColor(new java.awt.Color(116, 114, 114), workbook.getStylesSource().getIndexedColors());
            XSSFCellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            cellStyle.setFillBackgroundColor(customColor);
            cellStyle.setFillForegroundColor(customColor);
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cellStyle.setBorderBottom(BorderStyle.MEDIUM);
            cellStyle.setBottomBorderColor(IndexedColors.WHITE.getIndex());
            cellStyle.setBorderTop(BorderStyle.MEDIUM);
            cellStyle.setTopBorderColor(IndexedColors.WHITE.getIndex());
            cellStyle.setBorderLeft(BorderStyle.MEDIUM);
            cellStyle.setLeftBorderColor(IndexedColors.WHITE.getIndex());
            cellStyle.setBorderRight(BorderStyle.MEDIUM);
            cellStyle.setRightBorderColor(IndexedColors.WHITE.getIndex());
            cellStyle.setFont(fontHeader);

            XSSFRow row = sheet.createRow(0);
            cell = row.createCell(0);
            cell.setCellValue("Nombre de Áreas");
            cell.setCellStyle(cellStyle);

            for (CatArea ca : areas) {
                row = sheet.createRow(rowTable);
                cell = row.createCell(0);
                cell.setCellValue(ca.getDescription());

                rowTable++;
            }

            workbook.write(out);

            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException ex) {
            throw new RuntimeException("Error al descargar la plantilla de coordinadores", ex);
        } finally {
            try {
                resource.close();
            } catch (IOException ex) {
                throw new RuntimeException("Error al descargar la plantilla de coordinadores", ex);
            }
        }
    }

    @Override
    public void cargaMasivaCoordinadores(MultipartFile file, String user) {
        ResultadosCargaMasivaDTO resultados;
        CatArea area;
        CatCoordinator newCoordinator;
        CoordinadoresDTO errorCoordinator;
        List<CoordinadoresDTO> ListErrorCoordinator = new ArrayList<>();
        CatCoordinator inactiveCoordinator;
        Long idCoordinator = null;
        try {
            resultados = parsearDTO(file.getInputStream());

            ListErrorCoordinator = resultados.getCoordinadorErroneo();

            for (CoordinadoresDTO cDto : resultados.getCoordinadorCorrecto()) {
                if (!this.existeDuplicado(cDto.getCorreo(), user, 1)) {

                    inactiveCoordinator = this.reactivarCoordinador(cDto.getCorreo());

                    if (inactiveCoordinator != null) {
                        idCoordinator = inactiveCoordinator.getId();
                    }

                    area = areaRepository.findByDesciptionToUpperCase(cDto.getArea().toUpperCase().trim());

                    if (area != null) {
                        newCoordinator = CatCoordinator.builder()
                                .id(idCoordinator)
                                .name(cDto.getNombres() + " " + cDto.getApellidos())
                                .email(cDto.getCorreo())
                                .engineering(area)
                                .phone(cDto.getTelefono())
                                .gtimRole("Coordinador de área")
                                .managerName(cDto.getCoordinador())
                                .managerEmail(cDto.getCorreoCoordinador())
                                .status("Activo")
                                .createdBy(user)
                                .createdAt(LocalDateTime.now())
                                .active(true)
                                .build();

                        repository.save(newCoordinator);
                    } else {
                        errorCoordinator = CoordinadoresDTO.builder()
                                .nombres(cDto.getNombres())
                                .apellidos(cDto.getApellidos())
                                .correo(cDto.getCorreo())
                                .telefono(cDto.getTelefono())
                                .area(cDto.getArea())
                                .coordinador(cDto.getCoordinador())
                                .correoCoordinador(cDto.getCorreoCoordinador())
                                .resultado("La área seleccionado no existe en la base de datos")
                                .build();
                        ListErrorCoordinator.add(errorCoordinator);
                    }
                } else {
                    errorCoordinator = CoordinadoresDTO.builder()
                            .nombres(cDto.getNombres())
                            .apellidos(cDto.getApellidos())
                            .correo(cDto.getCorreo())
                            .telefono(cDto.getTelefono())
                            .area(cDto.getArea())
                            .coordinador(cDto.getCoordinador())
                            .correoCoordinador(cDto.getCorreoCoordinador())
                            .resultado("Ya existe un coordinador con estos datos")
                            .build();
                    ListErrorCoordinator.add(errorCoordinator);
                }
            }

            if (!ListErrorCoordinator.isEmpty()) {
                ByteArrayInputStream errorXls = excel.crearReporteErroresCoordinador(ListErrorCoordinator);
                if (errorXls != null) {
                    email.sendErroresCargaMasiva("coordinadores", user, new ByteArrayResource(errorXls.readAllBytes()));
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error al procesar archivo de carga masiva de coordinadores " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error al procesar archivo de carga masiva de coordinadores " + ex.getMessage(), ex);
        } finally {
            newCoordinator = null;
            errorCoordinator = null;
            ListErrorCoordinator = null;
        }
    }

    private ResultadosCargaMasivaDTO parsearDTO(InputStream inputStream) throws Exception {
        ValidatorUtils utilValidar = new ValidatorUtils();
        ResultadosCargaMasivaDTO resultado = new ResultadosCargaMasivaDTO();
        List<CoordinadoresDTO> coordinadoresCorrectos = new ArrayList<>();
        List<CoordinadoresDTO> coordinadoresErroneos = new ArrayList<>();
        CoordinadoresDTO fila;
        CoordinadoresDTO error;
        XSSFWorkbook wb = new XSSFWorkbook(inputStream);
        XSSFSheet xs = wb.getSheetAt(0);
        int rowNumber = 0;
        int cellIdx2 = 0, vacios = 0;
        boolean ignorar = false, plantillacorrecta = true;
        String telefono = "", resultadoError = "";
        StringBuilder errorDescripcion = new StringBuilder();
        CellType tipo = null;
        Cell currentCell = null;
        DataFormatter formatter = new DataFormatter();
        for (Row currentRow : xs) {
            fila = new CoordinadoresDTO();
            error = new CoordinadoresDTO();
            if (rowNumber == 0) {
                Iterator<Cell> cellsInRow = currentRow.iterator();
                while (cellsInRow.hasNext()) {
                    currentCell = cellsInRow.next();
                    tipo = currentCell.getCellType();
                    if (tipo != CellType.BLANK) {
                        if (tipo == CellType.STRING || tipo == CellType.NUMERIC) {
                            if (currentCell.getStringCellValue().toUpperCase().contains("ROL")) {
                                plantillacorrecta = false;
                            }
                        }
                    }
                }

                if (plantillacorrecta) {
                    rowNumber++;
                    continue;
                } else {
                    throw new BusinessException("Plantilla Erronea");
                }
            }

            if (rowNumber >= 1) {
                cellIdx2 = 0;
                vacios = 0;

                currentCell = currentRow.getCell(0);
                if (currentCell != null && currentCell.getCellType() != CellType.BLANK) {
                    if (currentCell.getCellType() == CellType.STRING) {
                        fila.setNombres(currentCell.getStringCellValue());
                        error.setNombres(currentCell.getStringCellValue());
                    } else {
                        error.setNombres(currentCell.getStringCellValue());
                        errorDescripcion.append("El nombre no era un valor valido,");
                        vacios++;
                    }
                } else {
                    error.setNombres("");
                    errorDescripcion.append("El nombre venia vacio,");
                    vacios++;
                }

                currentCell = currentRow.getCell(1);
                if (currentCell != null && currentCell.getCellType() != CellType.BLANK) {
                    if (currentCell.getCellType() == CellType.STRING) {
                        fila.setApellidos(currentCell.getStringCellValue());
                        error.setApellidos(currentCell.getStringCellValue());
                    } else {
                        error.setApellidos(currentCell.getStringCellValue());
                        errorDescripcion.append("El apellido no era un valor valido,");
                        vacios++;
                    }
                } else {
                    error.setApellidos("");
                    errorDescripcion.append("El apellido venia vacio,");
                    vacios++;
                }

                currentCell = currentRow.getCell(2);
                if (currentCell != null && currentCell.getCellType() != CellType.BLANK) {
                    if (currentCell.getCellType() == CellType.STRING) {
                        if (utilValidar.validarCorreo(currentCell.getStringCellValue())) {
                            fila.setCorreo(currentCell.getStringCellValue());
                            error.setCorreo(currentCell.getStringCellValue());
                        } else {
                            error.setCorreo(currentCell.getStringCellValue());
                            errorDescripcion.append("El correo no tenia el formato correcto,");
                            vacios++;
                        }
                    } else {
                        error.setCorreo(currentCell.getStringCellValue());
                        errorDescripcion.append("El correo no era un valor valido,");
                        vacios++;
                    }
                } else {
                    error.setCorreo("");
                    errorDescripcion.append("El correo venia vacio,");
                    vacios++;
                }

                currentCell = currentRow.getCell(3);
                if (currentCell != null && currentCell.getCellType() != CellType.BLANK) {
                    if (currentCell.getCellType() == CellType.STRING || currentCell.getCellType() == CellType.NUMERIC) {
                        if (currentCell.getCellType() == CellType.STRING) {
                            telefono = currentCell.getStringCellValue();
                        } else {
                            telefono = formatter.formatCellValue(currentCell);
                        }
                        if (utilValidar.validarTelefono(telefono)) {
                            fila.setTelefono(telefono);
                            error.setTelefono(telefono);
                        } else {
                            error.setTelefono(currentCell.getStringCellValue());
                            errorDescripcion.append("El télefono no tiene un formato correcto,");
                            vacios++;
                        }
                    } else {
                        error.setTelefono("");
                        errorDescripcion.append("El télefono no era un valor valido,");
                        vacios++;
                    }
                } else {
                    error.setTelefono("");
                    errorDescripcion.append("El télefono venia vacio,");
                    vacios++;
                }

                currentCell = currentRow.getCell(4);
                if (currentCell != null && currentCell.getCellType() != CellType.BLANK) {
                    if (currentCell.getCellType() == CellType.STRING) {
                        fila.setArea(currentCell.getStringCellValue());
                        error.setArea(currentCell.getStringCellValue());
                    } else {
                        error.setArea(currentCell.getStringCellValue());
                        errorDescripcion.append("El área no era un campo valido,");
                        vacios++;
                    }
                } else {
                    error.setArea("");
                    errorDescripcion.append("El área venia vacio,");
                    vacios++;
                }

                currentCell = currentRow.getCell(5);
                if (currentCell != null && currentCell.getCellType() != CellType.BLANK) {
                    if (currentCell.getCellType() == CellType.STRING) {
                        fila.setCoordinador(currentCell.getStringCellValue());
                        error.setCoordinador(currentCell.getStringCellValue());
                    } else {
                        error.setCoordinador(currentCell.getStringCellValue());
                        errorDescripcion.append("El Jefe Inmediato no era un campo valido,");
                        vacios++;
                    }
                } else {
                    error.setCoordinador(null);
                    fila.setCoordinador(null);
                }

                currentCell = currentRow.getCell(6);
                if (currentCell != null && currentCell.getCellType() != CellType.BLANK) {
                    if (currentCell.getCellType() == CellType.STRING) {
                        fila.setCorreoCoordinador(currentCell.getStringCellValue());
                        error.setCorreoCoordinador(currentCell.getStringCellValue());
                    } else {
                        error.setCorreoCoordinador(currentCell.getStringCellValue());
                        errorDescripcion.append("El correo del Jefe Inmediato no era un campo valido,");
                        vacios++;
                    }
                } else {
                    fila.setCorreoCoordinador(null);
                    error.setCorreoCoordinador(null);
                }
                if (vacios > 0) {
                    ignorar = true;
                }

                if (!ignorar) {
                    coordinadoresCorrectos.add(fila);
                } else {
                    resultadoError = errorDescripcion.toString();
                    error.setResultado(resultadoError.substring(0, resultadoError.length() - 1));
                    coordinadoresErroneos.add(error);
                }

                errorDescripcion = new StringBuilder();
            }

            ignorar = false;
            rowNumber++;
        }

        resultado.setCoordinadorCorrecto(coordinadoresCorrectos);
        resultado.setCoordinadorErroneo(coordinadoresErroneos);

        return resultado;
    }

    @Override
    public ResourceDeleteDTO getColaboradorAEliminar(Long resourceID, Long roleId) {
        CatCoordinator coordinator = repository.findById(resourceID)
                .orElseThrow(() -> new IllegalArgumentException("Coordinador no existe"));

        List<ProyectosColaboradorDTO> listProjects = resourceDAO.getProyectosXColaborador(roleId, resourceID);
        List<ColaboradoresCoordinadorDTO> listColaboradores = resourceDAO.getColaboradoresXCoordinador(resourceID);

        return ResourceDeleteDTO.builder()
                .id(coordinator.getId())
                .nombre(coordinator.getName())
                .correo(coordinator.getEmail())
                .idArea(coordinator.getEngineering().getId())
                .nombreArea(coordinator.getEngineering().getDescription())
                .proyectosAsignados(listProjects)
                .colaboradoresAsignados(listColaboradores)
                .build();
    }

    @Override
    public List<String> desactivarCoordinador(Long resourceIdAnt, Long roleId, Long resourceIdNew, String user) {

        List<String> cambios = resourceDAO.desactivarCoordinador(resourceIdAnt, roleId, resourceIdNew, user);

        return cambios;
    }

    private boolean existeDuplicado(String email, String userName, int cargaMasiva) {
        boolean existeResource = false;

        existeResource = repository.existsByEmailIgnoreCaseAndActiveTrue(email);

        if (!existeResource) {
            existeResource = resourceRepository.existsByEmailIgnoreCaseAndActiveTrue(email);
            if (existeResource) {
                if (cargaMasiva == 0) {
                    CatResource resource = resourceRepository.findByEmailIgnoreCaseAndActiveTrue(email);
                    LocalDate fechaActual = LocalDate.now(ZoneId.of("America/Mexico_City"));
                    Long totalAsignaciones = assignRepository.countByResourceIdAndRoleIdAndEndDateAfter(resource.getId(), resource.getRol().getId(), fechaActual);
                    if (Objects.nonNull(totalAsignaciones) && Objects.equals(totalAsignaciones, 0L)) {
                        resourceRepository.save(
                                CatResource.builder()
                                        .id(resource.getId())
                                        .firstName(resource.getFirstName())
                                        .lastName(resource.getLastName())
                                        .email(resource.getEmail())
                                        .phone(resource.getPhone())
                                        .area(resource.getArea())
                                        .rol(resource.getRol())
                                        .coordinador(resource.getCoordinador())
                                        .active(false)
                                        .createdBy(resource.getCreatedBy())
                                        .createdAt(resource.getCreatedAt())
                                        .updatedBy(userName)
                                        .updatedAt(LocalDateTime.now(ZoneId.of("America/Mexico_City")))
                                        .build()
                        );
                        existeResource = false;
                    } else {
                        throw new BusinessException("No se puede cambiar este colaborador a coordinador, ya que aún tiene asignaciones activas");
                    }
                } else {
                    existeResource = true;
                }
            }
        }

        return existeResource;
    }

    private CatCoordinator reactivarCoordinador(String email) {
        return repository.findByEmailIgnoreCaseAndActiveFalse(email);
    }
}
