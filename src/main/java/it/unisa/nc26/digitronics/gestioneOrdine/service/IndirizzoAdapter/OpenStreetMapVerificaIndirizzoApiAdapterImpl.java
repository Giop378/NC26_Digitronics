package it.unisa.nc26.digitronics.gestioneOrdine.service.IndirizzoAdapter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Implementazione dell'interfaccia VerificaIndirizzoApiAdapter che utilizza l'API di OpenStreetMap
 * (Nominatim) per verificare la validità di un indirizzo specificato.
 */
public class OpenStreetMapVerificaIndirizzoApiAdapterImpl implements VerificaIndirizzoApiAdapter {

    /**
     * Verifica se un indirizzo composto da via, CAP e città è valido utilizzando l'API di OpenStreetMap.
     *
     * @param via   il nome della via
     * @param cap   il codice di avviamento postale (CAP)
     * @param città il nome della città
     * @return true se l'indirizzo è valido, false altrimenti
     * @throws RuntimeException se si verifica un errore durante la connessione o l'elaborazione della risposta API
     */
    @Override
    public boolean verifica(String via, String cap, String città) {
        try {
            // Costruzione dell'URL per l'API di OpenStreetMap (Nominatim)
            String baseUrl = "https://nominatim.openstreetmap.org/search";
            String query = String.format("%s, %s, %s, Italia", via, cap, città);
            String urlStr = String.format("%s?q=%s&format=json&addressdetails=1&countrycodes=it",
                    baseUrl,
                    query.replace(" ", "+"));

            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; Digitronics/1.0)");

            // Lettura della risposta dall'API
            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new RuntimeException("Errore durante la connessione all'API di OpenStreetMap: " + responseCode);
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Parsing della risposta JSON
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response.toString());

            // Se l'array JSON è vuoto, l'indirizzo non esiste
            if (!rootNode.isArray() || rootNode.size() == 0) {
                return false;
            }

            // Verifica se l'indirizzo corrisponde alla via, CAP e città specificati
            for (JsonNode node : rootNode) {
                JsonNode address = node.get("address");
                if (address != null) {
                    String returnedCity = address.path("city").asText("").toLowerCase();
                    String returnedHamlet = address.path("hamlet").asText("").toLowerCase();
                    String returnedVillage = address.path("village").asText("").toLowerCase();
                    String returnedPostcode = address.path("postcode").asText("");
                    String returnedRoad = address.path("road").asText("").toLowerCase();

                    boolean cittàEsiste = città.toLowerCase().equals(returnedCity) ||
                            città.toLowerCase().equals(returnedHamlet) ||
                            città.toLowerCase().equals(returnedVillage);

                    if (cittàEsiste &&
                            returnedPostcode.equals(cap) &&
                            returnedRoad.contains(via.toLowerCase())) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Errore durante la verifica dell'indirizzo: " + e.getMessage(), e);
        }
        return false;
    }
}
