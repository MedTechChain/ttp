package nl.tudelft.medtechchain.ttp;

import nl.tudelft.medtechchain.ttp.encryption.paillier.PaillierDecryptionKey;
import nl.tudelft.medtechchain.ttp.encryption.paillier.PaillierEncryptionKey;
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

		// First we check if application.properties has a generated keypair defined
		// If it does, we create PaillierKeyPair object using the constructor and the defined values
		if (!ekN.equals("null") && !dkP.equals("null") && !dkQ.equals("null")) {
			try {
				PaillierEncryptionKey ek = new PaillierEncryptionKey(ekN);
				PaillierDecryptionKey dk = new PaillierDecryptionKey(dkP, dkQ);
				return new PaillierKeyPair(ek, dk);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// If it does not have a generated keypair defined, we use the rust paillier backend to
		// generate a keypair and then parse the json string it returns into a PaillierKeyPair object
		String jsonKeypair = scheme.generateKeypair();
		return new PaillierKeyPair(jsonKeypair);
	}

}
