
package com.clara.hopelife.integration.mercadopago;

import com.clara.hopelife.config.MercadoPagoProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MercadoPagoWebhookSignatureValidator {

    private final MercadoPagoProperties properties;

    public boolean validar(
            String assinatura,
            String requestId,
            String dataId
    ) {
        if (assinatura == null || requestId == null || dataId == null
                || assinatura.isBlank() || requestId.isBlank()
                || dataId.isBlank()) {
            return false;
        }

        String segredo = properties.getWebhookSecret();

        if (segredo == null || segredo.isBlank()) {
            return false;
        }

        try {
            Map<String, String> campos = new HashMap<>();

            for (String parte : assinatura.split(",")) {
                String[] chaveValor = parte.trim().split("=", 2);

                if (chaveValor.length == 2) {
                    campos.put(chaveValor[0], chaveValor[1]);
                }
            }

            String timestamp = campos.get("ts");
            String hashRecebido = campos.get("v1");

            if (timestamp == null || hashRecebido == null) {
                return false;
            }

            String manifesto = "id:" + dataId
                    + ";request-id:" + requestId
                    + ";ts:" + timestamp + ";";

            Mac mac = Mac.getInstance("HmacSHA256");

            SecretKeySpec chave = new SecretKeySpec(
                    segredo.getBytes(StandardCharsets.UTF_8),
                    "HmacSHA256"
            );

            mac.init(chave);

            byte[] hashCalculado = mac.doFinal(
                    manifesto.getBytes(StandardCharsets.UTF_8)
            );

            StringBuilder hexadecimal = new StringBuilder();

            for (byte b : hashCalculado) {
                hexadecimal.append(String.format("%02x", b & 0xff));
            }

            byte[] esperado = hexadecimal.toString()
                    .getBytes(StandardCharsets.UTF_8);

            byte[] recebido = hashRecebido
                    .getBytes(StandardCharsets.UTF_8);

            return MessageDigest.isEqual(esperado, recebido);

        } catch (Exception e) {
            return false;
        }
    }
}
