# MedTech Chain - Trusted Third Party

## Build encryption backend

Set working directory to Rust project and build binary using cargo build with release flag.

`cd paillier-encryption-backend`

`cargo build --release`

### Install Rust

You will probably need to install Rust if you don't have it

`curl --proto '=https' --tlsv1.2 -sSf https://sh.rustup.rs | sh`

Run `rustc --version` to verify installation.

## Running server

Use the run configuration in `run-configs/TtpApplication`

## Endpoints

### Get Encryption Key

`GET http://localhost:8000/api/key`

#### Request Headers

`None`

#### Example

##### Request

```shell
curl --location --request GET 'http://localhost:8000/api/key'
```

##### Response

This is the modulus of the encryption key, aka the public key.

`200 OK`

```json
{
    "n": "16500288134621604780270466834510655869142848674796908438718292571519355719112537274749860955501476710292896949667691723144412885043727734290320088672299108504395863322168428845345400459868407748601092653905444135343020059220417312981183528339991616469826391536342796896302910676225493646478685018700370320038231451993771600489193971620983993031245203575129372083542047549780216119517216302008020027547491735290810595248183547246456046621809361120863822159953598610897243015361329455789756256068794619001178083843033514159738577425553910499076885314600397059445862732926998450944397068752189995989517150075517394484133"
}
```
