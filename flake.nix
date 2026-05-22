{
  description = "KMP Dev simple charts";

  inputs = {
    # Grabs the latest stable version of Nix packages
    nixpkgs.url = "github:nixos/nixpkgs/nixos-unstable";
    # Helps handle multiple architectures (like Intel vs Apple Silicon if shared later)
    utils.url = "github:numtide/flake-utils";
  };

  outputs = {self, nixpkgs, utils}:
    utils.lib.eachDefaultSystem(system:
      let
        pkgs =import nixpkgs{ inherit system;};

        # Custom Gradle 9.5.1 build
        gradle_9_5_1_unwrapped = pkgs.gradle-unwrapped.overrideAttrs (oldAttrs: rec {
          version = "9.5.1";
          src = pkgs.fetchurl {
            url = "https://services.gradle.org/distributions/gradle-${version}-bin.zip";
            sha256 = "bafc141b619ad6350fd975fc903156dd5c151998cc8b058e8c1044ab5f7b031f";
          };
        });
      in
      {
        devShells.default = pkgs.mkShell {
          # Put the command-line tools your project needs here
          packages = with pkgs; [
            git
            jdk17
            kotlin
            gradle_9_5_1_unwrapped
          ];

          # Optional: Environment variables you want set when you enter the shell
          shellHook = ''
            export JAVA_HOME=${pkgs.jdk17}
            echo "⚡ Welcome to your Nix project environment! ⚡"
            echo "Java version: $(java -version 2>&1 | head -n 1)"
            echo "Kotlin version: $(kotlin -version | cut -d ' ' -f 3-)"
            echo "Gradle version: $(gradle -v | grep '^Gradle' | cut -d ' ' -f 2)"
          '';
        };
      });
}
