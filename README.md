# Crypto Solver - desktop GUI application for AP3KR Cryptology class at UTB Zlin

A modular, dark-themed desktop application built with **Kotlin** and **Java Swing** for solving, visualizing, and analyzing classical cryptographic algorithms.

> For classmates: please do not steal 🙏🥀

![Kotlin](https://img.shields.io/badge/Kotlin-1.9+-7F52FF?style=flat&logo=kotlin&logoColor=white)
![Swing](https://img.shields.io/badge/UI-Java%20Swing-orange?style=flat)
![License](https://img.shields.io/badge/License-MIT-blue.svg)

---

## Features

- **Launch menu**
- **1. Affine Cipher**
  - Full support for encryption and decryption ($E(x) = (a \cdot x + b) \bmod 26$)
  - **Live Alphabet Substitution Grid** 
  - **Text Normalization** 
  - **Keyword Formatting from custom dictionary**
  - **Key Validation** with support for positive/negative key normalizations
- **2. PlayFair Cipher**
  - Full support for encryption and decryption
  - Live 5x5 Matrix Substitution Grid
  - Multi-Language Alphabet Support (English: J=I, Czech: W=V)
  - Text Normalization & Filtering with smart separator insertion (X/Q)
  - Dynamic Key Processing & Keyword Mapping Support
  - Formatted Output chunked into 5-character blocks
- **3. ADFG(V)X Cipher**
  - Full support for encryption and decryption
  - option between 5x5 and 6x6 grids
  - Multi-Language Alphabet Support (English: J=I, Czech: W=V)
  - Text Normalization & Filtering
  - dynamic wildcard masking, including numbers in 5x5 grid mode
  - seed based table generation
  - Formatted Output chunked into 5-character blocks
- **4. RSA Cipher**
  - Full support for encryption and decryption
  - using Miller–Rabin primality test for validating prime numbers
  - local Key-Pair generation
  - user can set custom test depth and key length
  - option to use UTF-8 or UTF-16 encoding standard
  - masking message into blocks of 8/4 chars with 8/16 bits per character based on chosen encoding
  - chars out of range are masked with '?' flag
- more coming soon...

---

## Tech Stack & Architecture

- **Language:** Kotlin
- **UI Framework:** Java Swing
- **Pattern:** Clean Separation of Concerns (UI separation from cryptographic logic)

> This project is licensed under [MIT License](LICENSE).
