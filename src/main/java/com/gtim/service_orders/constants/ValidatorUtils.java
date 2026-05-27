package com.gtim.service_orders.constants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorUtils {

    public boolean validarCorreo(String email) {
        // Expresión regular básica para estructura de email
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);

        // Verifica si la sintaxis es correcta y si termina con el dominio específico
        return matcher.matches() && email.endsWith("@gtim.mx");
    }

    public boolean validarTelefono(String telefono) {
        // Expresión regular:
        // ^(\\+52|52)?  -> Opcional: +52 o 52
        // (1)?          -> Opcional: el '1' después del 52 (común en antiguos celulares)
        // \\d{10}$      -> Exactamente 10 dígitos numéricos
        String telefonoRegex = "^(\\+52|52)?(1)?\\d{10}$";

        Pattern pattern = Pattern.compile(telefonoRegex);
        Matcher matcher = pattern.matcher(telefono);

        // Verifica si la sintaxis es correcta y si termina con el dominio específico
        return matcher.matches();

    }

}
