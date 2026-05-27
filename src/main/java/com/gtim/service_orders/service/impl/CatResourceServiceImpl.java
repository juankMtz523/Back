package com.gtim.service_orders.service.impl;

import com.gtim.service_orders.constants.ValidatorUtils;
import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gtim.service_orders.dao.CatResourceDAO;
import com.gtim.service_orders.dto.CatResourceDTO;
import com.gtim.service_orders.dto.CatCoordinatorDTO;
import com.gtim.service_orders.dto.ResourceRequestDTO;
import com.gtim.service_orders.dto.ColaboradorDTO;
import com.gtim.service_orders.dto.ResourceDeleteDTO;
import com.gtim.service_orders.dto.ProyectosColaboradorDTO;
import com.gtim.service_orders.dto.ResultadosCargaMasivaDTO;
import com.gtim.service_orders.entity.CatResource;
import com.gtim.service_orders.entity.CatArea;
import com.gtim.service_orders.entity.CatRole;
import com.gtim.service_orders.entity.CatCoordinator;
import com.gtim.service_orders.exception.BusinessException;
import com.gtim.service_orders.mapper.CatResourceMapper;
import com.gtim.service_orders.mapper.CatCoordinatorMapper;
import com.gtim.service_orders.notification.EmailService;
import com.gtim.service_orders.notification.ExcelService;
import com.gtim.service_orders.repository.CatResourceRepository;
import com.gtim.service_orders.repository.CatCoordinatorRepository;
import com.gtim.service_orders.repository.CatAreaRepository;
import com.gtim.service_orders.repository.CatRoleRepository;
import com.gtim.service_orders.repository.ResourceAssignmentRepository;
import com.gtim.service_orders.service.CatResourceService;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Iterator;
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
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class CatResourceServiceImpl implements CatResourceService {

    private final CatResourceRepository repository;
    private final CatCoordinatorRepository coordinatorRepository;
    private final CatAreaRepository areaRepository;
    private final CatRoleRepository rolRepository;
    private final ResourceAssignmentRepository assignRepository;
    private final CatResourceMapper mapper;
    private final CatCoordinatorMapper coordinatorMapper;
    private final CatResourceDAO resourceDAO;
    private final ExcelService excel;
    private final EmailService email;

    @Override
    public List<CatResourceDTO> getResourcesNotAssignProposal(Long proposalId) {
        List<CatResourceDTO> colaboradores = resourceDAO.getResourceXProposal(proposalId);
        return colaboradores;
    }

    @Override
    public List<CatResourceDTO> getActiveResources() {

        CatResourceDTO resource;
        Long pctAssign;
        List<CatResourceDTO> listadoColaboradores = new ArrayList<>();
        Sort sortResource = Sort.by(Sort.Direction.ASC, "firstName");
        List<CatResourceDTO> resourcesActive = repository.findByActiveTrue(sortResource)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        Sort sortCoordinator = Sort.by(Sort.Direction.ASC, "name");
        List<CatCoordinatorDTO> coordinatorsActive = coordinatorRepository.findByActiveTrue(sortCoordinator)
                .stream()
                .map(coordinatorMapper::toDto)
                .collect(Collectors.toList());

        for (CatResourceDTO r : resourcesActive) {
            resource = r;
            pctAssign = assignRepository.callFunctionAsignacionResource(r.getId(), r.getRoleId());
            resource.setPctAsignacion((pctAssign != null ? pctAssign : 0L));
            listadoColaboradores.add(resource);
        }

        for (CatCoordinatorDTO cc : coordinatorsActive) {
            resource = new CatResourceDTO();
            resource.setId(cc.getId());
            resource.setFirstName(cc.getName());
            resource.setLastName("");
            resource.setEmail(cc.getEmail());
            resource.setPhone(cc.getPhone());
            resource.setAreaId(cc.getEngineeringId());
            resource.setAreaName(cc.getEngineeringName());
            resource.setRoleId(3L);
            resource.setRoleName("Coordinador de área");
            resource.setCoordinatorId(null);
            resource.setCoordinatorName(null);
            resource.setActive(Boolean.TRUE);

            pctAssign = assignRepository.callFunctionAsignacionResource(cc.getId(), 3L);
            resource.setPctAsignacion((pctAssign != null ? pctAssign : 0L));

            listadoColaboradores.add(resource);
        }

        listadoColaboradores.sort(Comparator.comparing(CatResourceDTO::getFirstName));
        return listadoColaboradores;
    }

    @Override
    public ResourceRequestDTO create(
            ResourceRequestDTO request,
            String user
    ) {

        Long idResource = null;

        if (!this.existeDuplicado(request.getEmail(), user, 0)) {

            CatResource inactiveResource = this.reactivarColaborador(request.getEmail());

            if (inactiveResource != null) {
                idResource = inactiveResource.getId();
            }

            CatArea area = areaRepository.findById(request.getAreaId())
                    .orElseThrow(() -> new BusinessException("Area no encontrado"));

            CatRole rol = rolRepository.findById(request.getRoleId())
                    .orElseThrow(() -> new BusinessException("Rol no encontrado"));

            CatCoordinator coordinador = coordinatorRepository.findById(request.getCoordinatorId())
                    .orElseThrow(() -> new BusinessException("Coordinador no encontrado"));

            CatResource newResource = CatResource.builder()
                    .id(idResource)
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .phone(request.getPhone())
                    .area(area)
                    .rol(rol)
                    .coordinador(coordinador)
                    .active(true)
                    .createdBy(user)
                    .createdAt(LocalDateTime.now())
                    .build();

            CatResource saved = repository.save(newResource);

            return ResourceRequestDTO.builder()
                    .id(saved.getId())
                    .firstName(saved.getFirstName())
                    .lastName(saved.getLastName())
                    .email(saved.getEmail())
                    .phone(saved.getPhone())
                    .areaId(request.getAreaId())
                    .roleId(request.getRoleId())
                    .coordinatorId(request.getCoordinatorId())
                    .build();
        } else {
            throw new BusinessException("Ya existe un colaborador con ese correo");
        }
    }

    @Override
    public ResourceRequestDTO update(
            ResourceRequestDTO request,
            String user
    ) {

        if (!Objects.equals(request.getId(), 0L)) {
            CatResource resource = repository.findById(request.getId())
                    .orElseThrow(() -> new BusinessException("Colaborador no existe"));

            CatArea area = areaRepository.findById(request.getAreaId())
                    .orElseThrow(() -> new BusinessException("Area no encontrado"));

            CatRole rol = rolRepository.findById(request.getRoleId())
                    .orElseThrow(() -> new BusinessException("Rol no encontrado"));

            CatCoordinator coordinador = coordinatorRepository.findById(request.getCoordinatorId())
                    .orElseThrow(() -> new BusinessException("Coordinador no encontrado"));

            CatResource updResource = CatResource.builder()
                    .id(resource.getId())
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .phone(request.getPhone())
                    .area(area)
                    .rol(rol)
                    .coordinador(coordinador)
                    .active(true)
                    .createdBy(resource.getCreatedBy())
                    .createdAt(resource.getCreatedAt())
                    .updatedBy(user)
                    .updatedAt(LocalDateTime.now())
                    .build();

            CatResource saved = repository.save(updResource);

            return ResourceRequestDTO.builder()
                    .id(saved.getId())
                    .firstName(saved.getFirstName())
                    .lastName(saved.getLastName())
                    .email(saved.getEmail())
                    .phone(saved.getPhone())
                    .areaId(request.getAreaId())
                    .roleId(request.getRoleId())
                    .coordinatorId(request.getCoordinatorId())
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
            List<CatRole> roles = rolRepository.findByActiveTrue(sort);
            sort = Sort.by(Sort.Direction.ASC, "name");
            List<CatCoordinator> coordinadores = coordinatorRepository.findByActiveTrue(sort);

            resource = new ClassPathResource("plantilla_colaboradores.xlsx").getInputStream();
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
            cell.setCellValue("Áreas");
            cell.setCellStyle(cellStyle);

            for (CatArea ca : areas) {
                row = sheet.createRow(rowTable);
                cell = row.createCell(0);
                cell.setCellValue(ca.getDescription());

                rowTable++;
            }

            rowTable++;
            row = sheet.createRow(rowTable);
            cell = row.createCell(0);
            cell.setCellValue("Roles");
            cell.setCellStyle(cellStyle);
            rowTable++;

            for (CatRole cr : roles) {
                row = sheet.createRow(rowTable);
                cell = row.createCell(0);
                cell.setCellValue(cr.getDescription());

                rowTable++;
            }

            rowTable++;
            row = sheet.createRow(rowTable);
            cell = row.createCell(0);
            cell.setCellValue("Coordinadores");
            cell.setCellStyle(cellStyle);
            rowTable++;

            for (CatCoordinator cc : coordinadores) {
                row = sheet.createRow(rowTable);
                cell = row.createCell(0);
                cell.setCellValue(cc.getName());

                rowTable++;
            }

            workbook.write(out);

            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException ex) {
            throw new RuntimeException("Error al descargar la plantilla de colaboradores", ex);
        } finally {
            try {
                resource.close();
            } catch (IOException ex) {
                throw new RuntimeException("Error al descargar la plantilla de colaboradores", ex);
            }
        }
    }

    @Override
    public void cargaMasivaColaboaradores(MultipartFile file, String user) {
        ResultadosCargaMasivaDTO resultados;
        CatArea area;
        CatRole rol;
        CatCoordinator coordinador;
        ColaboradorDTO errorColaborador;
        List<ColaboradorDTO> ListErrorColaborador = new ArrayList<>();
        CatResource newResource;
        CatResource inactiveResource;
        Long idResource = null;
        String resultado = "";
        try {
            resultados = parsearDTO(file.getInputStream());

            ListErrorColaborador = resultados.getColaboradorErroneo();

            for (ColaboradorDTO cDto : resultados.getColaboradorCorrecto()) {
                resultado = "";
                if (!this.existeDuplicado(cDto.getCorreo(), user, 1)) {

                    inactiveResource = this.reactivarColaborador(cDto.getCorreo());

                    if (inactiveResource != null) {
                        idResource = inactiveResource.getId();
                    }

                    area = areaRepository.findByDesciptionToUpperCase(cDto.getArea().toUpperCase().trim());
                    rol = rolRepository.findByDesciptionToUpperCase(cDto.getRolGtim().toUpperCase().trim());
                    coordinador = coordinatorRepository.findByNameToUpperCase(cDto.getCoodrinador().toUpperCase().trim());

                    if (area != null && rol != null && coordinador != null) {
                        newResource = CatResource.builder()
                                .id(idResource)
                                .firstName(cDto.getNombres())
                                .lastName(cDto.getApellidos())
                                .email(cDto.getCorreo())
                                .phone(cDto.getTelefono())
                                .area(area)
                                .rol(rol)
                                .coordinador(coordinador)
                                .active(true)
                                .createdBy(user)
                                .createdAt(LocalDateTime.now())
                                .build();
                        repository.save(newResource);
                    } else {
                        if (area == null) {
                            resultado = resultado + "El área seleccionada no existe, ";
                        }
                        if (rol == null) {
                            resultado = resultado + "El rol seleccionado no existe, ";
                        }
                        if (coordinador == null) {
                            resultado = resultado + "El coordinador seleccionado no existe, ";
                        }
                        errorColaborador = ColaboradorDTO.builder()
                                .nombres(cDto.getNombres())
                                .apellidos(cDto.getApellidos())
                                .correo(cDto.getCorreo())
                                .telefono(cDto.getTelefono())
                                .area(cDto.getArea())
                                .rolGtim(cDto.getRolGtim())
                                .coodrinador(cDto.getCoodrinador())
                                .resultado(resultado.substring(0, resultado.length() - 2))
                                .build();
                        ListErrorColaborador.add(errorColaborador);
                    }
                } else {
                    errorColaborador = ColaboradorDTO.builder()
                            .nombres(cDto.getNombres())
                            .apellidos(cDto.getApellidos())
                            .correo(cDto.getCorreo())
                            .telefono(cDto.getTelefono())
                            .area(cDto.getArea())
                            .rolGtim(cDto.getRolGtim())
                            .coodrinador(cDto.getCoodrinador())
                            .resultado("Ya existe un colaborador con estos datos")
                            .build();
                    ListErrorColaborador.add(errorColaborador);
                }
            }

            if (!ListErrorColaborador.isEmpty()) {
                ByteArrayInputStream errorXls = excel.crearReporteErroresColaborador(ListErrorColaborador);
                if (errorXls != null) {
                    email.sendErroresCargaMasiva("colaboradores", user, new ByteArrayResource(errorXls.readAllBytes()));
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error al procesar archivo de carga masiva de colaboradores " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error al procesar archivo de carga masiva de colaboradores " + ex.getMessage(), ex);
        } finally {
            resultados = null;
            area = null;
            rol = null;
            coordinador = null;
            errorColaborador = null;
            ListErrorColaborador = null;
            newResource = null;
            inactiveResource = null;
        }
    }

    private ResultadosCargaMasivaDTO parsearDTO(InputStream inputStream) throws Exception {
        ValidatorUtils utilValidar = new ValidatorUtils();
        ResultadosCargaMasivaDTO resultado = new ResultadosCargaMasivaDTO();
        List<ColaboradorDTO> colaboradoresCorrecto = new ArrayList<>();
        List<ColaboradorDTO> colaboradoresErroneo = new ArrayList<>();
        ColaboradorDTO filas;
        ColaboradorDTO error;
        XSSFWorkbook wb = new XSSFWorkbook(inputStream);
        XSSFSheet xs = wb.getSheetAt(0);
        int rowNumber = 0;
        int cellIdx2 = 0, vacios = 0;
        String telefono = "", resultadoError = "";
        StringBuilder errorDescripcion = new StringBuilder();
        boolean ignorar = false, plantillacorrecta = false;
        Cell currentCell = null;
        CellType tipo = null;
        DataFormatter formatter = new DataFormatter();
        for (Row currentRow : xs) {
            filas = new ColaboradorDTO();
            error = new ColaboradorDTO();
            if (rowNumber == 0) {
                Iterator<Cell> cellsInRow = currentRow.iterator();
                while (cellsInRow.hasNext()) {
                    currentCell = cellsInRow.next();
                    tipo = currentCell.getCellType();
                    if (tipo != CellType.BLANK) {
                        if (tipo == CellType.STRING || tipo == CellType.NUMERIC) {
                            if (currentCell.getStringCellValue().toUpperCase().contains("ROL")) {
                                plantillacorrecta = true;
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
                        filas.setNombres(currentCell.getStringCellValue());
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
                        filas.setApellidos(currentCell.getStringCellValue());
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
                            filas.setCorreo(currentCell.getStringCellValue());
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
                            filas.setTelefono(telefono);
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
                        filas.setArea(currentCell.getStringCellValue());
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
                        filas.setRolGtim(currentCell.getStringCellValue());
                        error.setRolGtim(currentCell.getStringCellValue());
                    } else {
                        error.setRolGtim(currentCell.getStringCellValue());
                        errorDescripcion.append("El Rol GTIM no era un campo valido,");
                        vacios++;
                    }
                } else {
                    error.setRolGtim("");
                    errorDescripcion.append("El Rol GTIM venia vacio,");
                    vacios++;
                }

                currentCell = currentRow.getCell(6);
                if (currentCell != null && currentCell.getCellType() != CellType.BLANK) {
                    if (currentCell.getCellType() == CellType.STRING) {
                        filas.setCoodrinador(currentCell.getStringCellValue());
                        error.setCoodrinador(currentCell.getStringCellValue());
                    } else {
                        error.setCoodrinador(currentCell.getStringCellValue());
                        errorDescripcion.append("El coordinador no era un campo valido,");
                        vacios++;
                    }
                } else {
                    error.setCoodrinador("");
                    errorDescripcion.append("El coordinador venia vacio,");
                    vacios++;
                }

                if (vacios > 0) {
                    ignorar = true;
                }

                if (!ignorar) {
                    colaboradoresCorrecto.add(filas);
                } else {
                    resultadoError = errorDescripcion.toString();
                    error.setResultado(resultadoError.substring(0, resultadoError.length() - 1));
                    colaboradoresErroneo.add(error);
                }

                errorDescripcion = new StringBuilder();
            }

            ignorar = false;
            rowNumber++;
        }

        resultado.setColaboradorCorrecto(colaboradoresCorrecto);
        resultado.setColaboradorErroneo(colaboradoresErroneo);

        return resultado;
    }

    @Override
    public ResourceDeleteDTO getColaboradorAEliminar(Long resourceID, Long roleId) {
        CatResource resource = repository.findById(resourceID)
                .orElseThrow(() -> new IllegalArgumentException("Colaborador no existe"));

        List<ProyectosColaboradorDTO> listProjects = resourceDAO.getProyectosXColaborador(roleId, resourceID);

        return ResourceDeleteDTO.builder()
                .id(resource.getId())
                .nombre(resource.getFirstName() + " " + resource.getLastName())
                .correo(resource.getEmail())
                .idArea(resource.getArea().getId())
                .nombreArea(resource.getArea().getName())
                .proyectosAsignados(listProjects)
                .colaboradoresAsignados(null)
                .build();
    }

    @Override
    public List<String> desactivarColaborador(Long resourceIdAnt, String user) {

        List<String> cambios = new ArrayList<>();

        cambios = resourceDAO.desactivarColaborador(resourceIdAnt, user);

        return cambios;
    }

    private boolean existeDuplicado(String email, String user, int cargaMasiva) {
        boolean existeResource = false;

        existeResource = repository.existsByEmailIgnoreCaseAndActiveTrue(email);

        if (!existeResource) {
            existeResource = coordinatorRepository.existsByEmailIgnoreCaseAndActiveTrue(email);
            if (existeResource) {
                if (cargaMasiva == 0) {
                    CatCoordinator coordinator = coordinatorRepository.findByEmailIgnoreCaseAndActiveTrue(email);
                    LocalDate fechaActual = LocalDate.now(ZoneId.of("America/Mexico_City"));
                    Long totalAsignaciones = assignRepository.countByResourceIdAndRoleIdAndEndDateAfter(coordinator.getId(), 3L, fechaActual);
                    if (Objects.nonNull(totalAsignaciones) && Objects.equals(totalAsignaciones, 0L)) {
                        coordinatorRepository.save(
                                CatCoordinator.builder()
                                        .id(coordinator.getId())
                                        .name(coordinator.getName())
                                        .email(coordinator.getEmail())
                                        .engineering(coordinator.getEngineering())
                                        .phone(coordinator.getPhone())
                                        .gtimRole(coordinator.getGtimRole())
                                        .managerName(coordinator.getManagerName())
                                        .managerEmail(coordinator.getManagerEmail())
                                        .status(coordinator.getStatus())
                                        .active(false)
                                        .createdBy(coordinator.getCreatedBy())
                                        .createdAt(coordinator.getCreatedAt())
                                        .updatedBy(user)
                                        .updatedAt(LocalDateTime.now(ZoneId.of("America/Mexico_City")))
                                        .build()
                        );
                    } else {
                        throw new BusinessException("No se puede cambiar este coordinador a colaborador, ya que aún tiene asignaciones activas");
                    }
                } else {
                    existeResource = true;
                }
            }
        }

        return existeResource;
    }

    private CatResource reactivarColaborador(String email) {
        return repository.findByEmailIgnoreCaseAndActiveFalse(email);
    }
}
