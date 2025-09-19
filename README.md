# CAPTCHA Service for Messaging Systems

A lightweight **Java-based CAPTCHA service** designed to integrate seamlessly into messaging systems, enhancing security by preventing automated bot interactions.

---

## Features

- **Simple Integration**: Easily incorporate CAPTCHA functionality into your messaging system.
- **Customizable Settings**: Adjust CAPTCHA parameters to fit your system's requirements.
- **Minimal Dependencies**: Built with core Java libraries, ensuring broad compatibility.

---

## Installation

Clone the repository to your local machine:

```bash
git clone https://github.com/kangaroo512/catpcha-service.git


## Usage

### Generate a CAPTCHA

To generate a new CAPTCHA image and get its text:

```java
// Create a new instance of the CaptchaService
CaptchaService captchaService = new CaptchaService();

// Generate a CAPTCHA
Captcha captcha = captchaService.generateCaptcha();

// Retrieve the CAPTCHA image and text
BufferedImage captchaImage = captcha.getImage();
String captchaText = captcha.getText();

// You can now display the image in your UI or save it to a file

### Validate a CAPTCHA

To check if the user’s input matches the generated CAPTCHA:

```java
// userInput is the text entered by the user
boolean isValid = captchaService.validateCaptcha(userInput, captchaText);

if (isValid) {
    System.out.println("CAPTCHA validated successfully!");
} else {
    System.out.println("CAPTCHA validation failed.");
}

