## Run UI tests
- IntelliJ: run `testng-ui.xml`
- CLI: `mvn test -Dsurefire.suiteXmlFiles=./testng-ui.xml -Dbrowser=chrome`

## Run API tests
- IntelliJ: run `testng-api.xml`
- CLI: `mvn test -Dsurefire.suiteXmlFiles=./testng-api.xml -Dapi.baseUrl=https://petstore.swagger.io/v2`