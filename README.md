FIAP - Serverless aws
Turma: 35SCJ

Professor: Peterson Larentis

Grupo:

335444 - Andre Brito
335196 - Leonardo Vieira
335537 - Eduardo Sukeda
335907 - Marcos Moura

Requisitos

AWS CLI já configurada com pelo menos permissões de "Power User"
JDK 8 instalado
Docker instalado
Maven
SAM CLI
Python 3

Clonar o repositório do GitHub:

git clone https://github.com/marcosautomacao/aws-labda-dynamo-trip-store.git
Instalar o pacote e suas dependências no ambiente local utilizando Maven:

mvn clean install -X -U

ATENÇÃO: O projeto esta com uma coleção do postman pronta para acessar os serviços
disponibilizados pela api em src\test\resources\Trips.postman_collection.json

Subir localmente uma Imagem Docker do DynamoDB:

docker run -p 8000:8000 -v $(pwd)/local/dynamodb:/data/ amazon/dynamodb-local -jar DynamoDBLocal.jar -sharedDb -dbPath /data
Criar a tabela "trip" no DynamoDB:

aws dynamodb create-table --table-name trip 
--attribute-definitions     
    AttributeName=country,AttributeType=S 
    AttributeName=dateTrip,AttributeType=S 
    AttributeName=city,AttributeType=S 
    AttributeName=reason,AttributeType=S 
--key-schema
    AttributeName=country,KeyType=HASH AttributeName=dateTrip,KeyType=RANGE 
--local-secondary-indexes 
    'IndexName=cityIndex,KeySchema=[{AttributeName=country,KeyType=HASH},{AttributeName=city,KeyType=RANGE}],Projection={ProjectionType=ALL}' 
    'IndexName=reasonIndex,KeySchema=[{AttributeName=country,KeyType=HASH},{AttributeName=reason,KeyType=RANGE}],Projection={ProjectionType=ALL}' 
--billing-mode PAY_PER_REQUEST --endpoint-url http://192.168.0.106:8000

Observação: Caso queira excluir a tabela, execute o comando:
aws dynamodb delete-table --table-name trip --endpoint-url http://localhost:8000

Para subir a api pelo SAM:

Mac:
sam local start-api --env-vars src/test/resources/test_environment_mac.json
Windows:
sam local start-api --env-vars src/test/resources/test_environment_windows.json
Linux:
sam local start-api --env-vars src/test/resources/test_environment_linux.json
Importe o arquivo /test/resources/Trip.postman_collection.json como collection no Postman para consumir os endpoints da aplicação.

Empacotar e subir aplicação para o AWS S3

Criar um Bucket no S3:

export BUCKET_NAME=fiap-servless-trip-35scj && aws s3 mb s3://$BUCKET_NAME
Empacotar a aplicação e disponibilizá-la no Bucket S3:

sam package \
--template-file template.yaml \
--output-template-file packaged.yaml \
--s3-bucket $BUCKET_NAME

Iniciar o Cloud Formation para criar nossos recursos na AWS:

sam deploy \
--template-file packaged.yaml \
--stack-name trip \
--capabilities CAPABILITY_IAM