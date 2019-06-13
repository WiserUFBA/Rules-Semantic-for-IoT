# Rules-Semantic-for-IoT
This module is responsible for making inferences about the user-generated rules

# Introduction
The soft-iot-semantic-reasoner module is responsible for making inferences from the jenaframe about the rules created by the user. When the soft-iot-semantic-reasoner module executes a rule and infers that a condition has been satisfied, it performs an update in the model. This update is performed only on the RDF triples that meet the rule using SPARQL UPDATE.

# Installation

Before installing the soft-iot-semantic-reasoner module you need to introduce some modules to ServiceMix. The dependencies are located at:

https://github.com/WiserUFBA/Rules-Semantic-for-IoT/tree/master/Rules-Semantic-for-IoT/src/main/resources/dependencies


# Deployment
For installation of Semantic-Observer it is necessary to execute the following commands in the ServiceMix terminal:


karaf@root()> bundle:install mvn:br.dcc.ufba.wiser.smartufba.reasoner/Rules-Semantic-for-IoT/

## Support and development

<p align="center">
	Developed by Cleber Jorge Lira de Santana at </br>
  <img src="https://wiki.dcc.ufba.br/pub/SmartUFBA/ProjectLogo/wiserufbalogo.jpg"/>
</p>


