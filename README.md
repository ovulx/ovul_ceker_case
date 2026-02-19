# Automation Case - Övül Çeker

This repository contains a small automation project that covers:
- **UI Web Automation (Selenium + TestNG)**
- **API Automation (Rest Assured + TestNG)**
- **JMeter Load Test Plan (.jmx)**

---

## Project Structure
- `AutomationTask/` → UI + API automation project (Maven)
- `loadtest.jmx` → JMeter test plan

---

## Tech Stack
- Java
- Maven
- TestNG
- Selenium WebDriver
- Rest Assured
- JMeter

---

## Prerequisites
- Java 17+
- Maven 3.8+
- Google Chrome installed (for UI tests)
- (Optional) Firefox installed (for Firefox UI test suite)
- JMeter 5.6+ (to run the `.jmx` file)

---

## How to Run

Run commands from the repository root.

1) UI (Chrome)
- Open AutomationTask folder
- Run: mvn clean test -DsuiteXmlFile=testng-ui.xml

2) UI (Firefox) - optional
- Run: mvn clean test -DsuiteXmlFile=testng-ui-firefox.xml

3) API
- Run: mvn clean test -DsuiteXmlFile=testng-api.xml

4) JMeter (CLI) - optional
- Go back to repository root (one folder up)
- Run: jmeter -n -t loadtest.jmx -l results.jtl

---

## Notes / Known Issue (JMeter)
The JMeter test plan targets n11.com search/listing endpoints.
During execution, responses may return 301/403 depending on the network/IP and Cloudflare protection rules.
The same request returns 200 OK in Postman with valid Cloudflare cookies, but JMeter may not consistently reproduce the same behavior in some environments.
The .jmx file is provided as requested; results can vary based on the runtime environment.
