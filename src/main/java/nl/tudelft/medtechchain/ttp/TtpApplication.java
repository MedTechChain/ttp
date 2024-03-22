package nl.tudelft.medtechchain.ttp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nl.tudelft.medtechchain.ttp.encryption.paillier.PaillierHomomorphicEncryptionScheme;
import nl.tudelft.medtechchain.ttp.encryption.paillier.PaillierKeyPair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

// TODO: document and improve this code :)

@SpringBootApplication
public class TtpApplication {

	@Value("${paillier.ek.n:null}")
	private String ekN;

	@Value("${paillier.dk.p:null}")
	private String dkP;

	@Value("${paillier.dk.q:null}")
	private String dkQ;

	public static void main(String[] args) {
		SpringApplication.run(TtpApplication.class, args);
	}

	@Bean
	public PaillierKeyPair keyPair() {
		PaillierHomomorphicEncryptionScheme scheme = new PaillierHomomorphicEncryptionScheme();
		if (!ekN.equals("null") && !dkP.equals("null") && !dkQ.equals("null")) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				ObjectNode keyPairNode = mapper.createObjectNode();
				keyPairNode.put("encryptionKey", "{\"n\":\"" + ekN + "\"}");
				keyPairNode.put("decryptionKey", "{\"p\":\"" + dkP + "\",\"q\":\"" + dkQ + "\"}");
				String keyPairJson = mapper.writeValueAsString(keyPairNode);
				return new PaillierKeyPair(keyPairJson);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String jsonKeypair = scheme.generateKeypair();
		return new PaillierKeyPair(jsonKeypair);
	}

}
