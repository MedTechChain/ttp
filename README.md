# MedTech Chain - Trusted Third Party

## Build encryption backend

Run `scripts/copy-lib.sh`

Do this before running the server.

<!-- ## Build encryption backend

Set working directory to Rust project and build binary using cargo build with release flag.

`cd paillier-encryption-backend`

`cargo build --release`

### Install Rust

You will probably need to install Rust if you don't have it

`curl --proto '=https' --tlsv1.2 -sSf https://sh.rustup.rs | sh`

Run `rustc --version` to verify installation. -->

## Run the server

### Docker

`docker-compose -p medtechchain-ttp up -d --build`

### IntelliJ

Use the run configuration in `run-configs/TtpApplication`
