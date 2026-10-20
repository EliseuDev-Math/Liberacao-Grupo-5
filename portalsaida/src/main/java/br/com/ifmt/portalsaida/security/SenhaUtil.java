package br.com.ifmt.portalsaida.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/** Hash SHA-256 com sal fixo (didático). Em produção, prefira BCrypt (Spring Security). */
public final class SenhaUtil {
    private static final String SAL = "portalsaida-ifmt:";
    private SenhaUtil() { }

    public static String hash(String senha) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(md.digest((SAL + senha).getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
