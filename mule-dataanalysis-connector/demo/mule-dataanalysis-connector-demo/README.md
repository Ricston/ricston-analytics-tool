# DataAnalysis Anypoint Connector Demo

Mule Studio demo for DataAnalysis connector.

How to Run Demo

Import the project folder in Studio, specify your host and port in /src/main/resources/ports.properties and run the application. Please note that this connector is only one part of a bigger system. Follow the details here https://github.com/Ricston/ricston-analytics-tool/wiki for information about the other components.

##About the Demo

The demo includes the following options:

- Exposes web service on http://localhost:8091/register
  - WSDL at: http://localhost:8091/register?wsdl
- Every time the web service is called, the age is stored for analysis
- Follow the details shown in the wiki page to set up the whole system and be able to see the analytics being collected

