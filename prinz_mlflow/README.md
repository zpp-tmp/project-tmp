# Prinz Mlflow integration

## Mlflow responsibilities

Mlflow is an opensource project for managing machine learning models lifecycle, including:

1. Experimentation and reproducibility - it allows to convert many machine learning models format to standardized model format. User of Mlflow gets the ability to record and query experiments in standard formats which simplifies the reproduction process of model deployment

2. Deployment - it allows to real-time serve the logged models as the webservices. There are integrations with well known platforms for machine learning including Microsoft Azure ML, Amazon SageMaker and Apache Spark UDF 

3. Central model registry - it can be used as a single repository with dedicated web application for model versioning. It allows to keep models versions in single place and make them easily accessible for all developers in organization.

## Mlfow environment

To work with Mlflow developer need some instance of Mlflow server which would seerve as the models registry. Additionally, there are standalone webservices for realtime models quering using web requests.

The whole environment in this repository is build from parts defined in docker-compose.yaml file which includes the definition for

1. `mlflow-server` which works as the Mlflow model registry server and can be queried for already trained models and their train data specification. It has also the information about the location of models signature location (which is a standalone S3 bucket)

2. `postgres-mlflow` is a database supporting the `mlflow-server` backend.

3. `aws-mlflow` acts as a S3 bucket server for holding models artifact storage

4. `proxy` keeps all the containers organized in a way that simulates the real-world example of models deployment.

To start the environment with the Nussknacker deployed use the create_environment.sh script. 

## Model serving process

When the Mlflow environment is created there are sample models trained. They are just samples which aren't provided as real-world usage examples so shouldn't be used in production.


