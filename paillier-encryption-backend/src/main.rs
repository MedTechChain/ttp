extern crate paillier;
extern crate serde_json;
use clap::{Parser, Subcommand};
use paillier::*;
use serde_json::json;

#[derive(Parser, Debug)]
#[command(author = None, version, about = None, long_about = None)]
#[command(propagate_version = true)]
struct Cli {
    #[command(subcommand)]
    command: Commands,
}

#[derive(Subcommand, Debug)]
enum Commands {
    /// Generates a new keypair and prints them
    Keygen,
    /// Encrypts a given plaintext with the provided encryption key
    Encrypt {
        /// Plaintext to encrypt
        #[arg(short, long)]
        plaintext: u64,
        /// Encryption key in JSON format
        #[arg(short, long)]
        ek: String,
    },
    /// Decrypts a given ciphertext with the provided decryption key
    Decrypt {
        /// Ciphertext to decrypt
        #[arg(short, long)]
        ciphertext: String,
        /// Decryption key in JSON format
        #[arg(short, long)]
        dk: String,
    },
}

fn main() {
    let cli = Cli::parse();

    match &cli.command {
        Commands::Keygen => {
            let (ek, dk) = generate_keys();
            let response = json!({
                "encryptionKey": serde_json::to_string(&ek).expect("Failed to serialize EncryptionKey"),
                "decryptionKey": serde_json::to_string(&dk).expect("Failed to serialize DecryptionKey")
            });
            println!("{}", response);
        }
        Commands::Encrypt { plaintext, ek } => {
            let ek: EncryptionKey =
                serde_json::from_str(ek).expect("Failed to deserialize EncryptionKey");
            let ciphertext = encrypt(&ek, *plaintext);
            let response = json!({
                "ciphertext": serde_json::to_string(&ciphertext).expect("Failed to serialize ciphertext"),
            });
            println!("{}", response);
        }
        Commands::Decrypt { ciphertext, dk } => {
            let dk: DecryptionKey =
                serde_json::from_str(dk).expect("Failed to deserialize DecryptionKey");
            let ciphertext: EncodedCiphertext<u64> =
                serde_json::from_str(ciphertext).expect("Failed to deserialize ciphertext");
            let plaintext = decrypt(&dk, ciphertext);
            let response = json!({
                "plaintext": plaintext,
            });
            println!("{}", response);
        }
    }
}

fn generate_keys() -> (EncryptionKey, DecryptionKey) {
    let (ek, dk) = Paillier::keypair().keys();

    // println!("EncryptionKey: {:?}", ek);
    // println!("DecryptionKey: {:?}", dk);
    // // Serialize the EncryptionKey
    // let serialized_ek = serde_json::to_string(&ek).expect("Failed to serialize EncryptionKey");
    // // Serialize the DecryptionKey
    // let serialized_dk = serde_json::to_string(&dk).expect("Failed to serialize DecryptionKey");
    // println!("Serialized EncryptionKey: {}", serialized_ek);
    // println!("Serialized DecryptionKey: {}", serialized_dk);

    // // Deserialize the EncryptionKey
    // let deserialized_ek: EncryptionKey =
    //     serde_json::from_str(&serialized_ek).expect("Failed to deserialize EncryptionKey");
    // println!("Deserialized EncryptionKey: {:?}", deserialized_ek);

    // // Deserialize the DecryptionKey
    // let deserialized_dk: DecryptionKey =
    //     serde_json::from_str(&serialized_dk).expect("Failed to deserialize DecryptionKey");
    // println!("Deserialized DecryptionKey: {:?}", deserialized_dk);

    (ek, dk)
}

fn encrypt(ek: &EncryptionKey, m: u64) -> EncodedCiphertext<u64> {
    Paillier::encrypt(ek, m)
}

fn decrypt(dk: &DecryptionKey, c: EncodedCiphertext<u64>) -> u64 {
    Paillier::decrypt(dk, c)
}
