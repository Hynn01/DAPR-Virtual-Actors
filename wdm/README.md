
# Dapr Virtual Actors Microservice 

Microservices implemented with Dapr Virtual Actors and Redis as backend database.

## Architecture
![image](https://user-images.githubusercontent.com/26082974/122112950-b7edf500-ce21-11eb-9367-c7583e1fde52.png)


## Project Structure
```
/components/
└── redis.yaml

/deployment/
├── docker-compose
│   ├── deploy
│   │   ├── components
│   │   │   └── redis.yaml
│   │   ├── orderservice
│   │   │   ├── Dockerfile
│   │   │   └── target
│   │   ├── paymentservice
│   │   │   ├── Dockerfile
│   │   │   └── target
│   │   ├── stockservice
│   │   │   ├── Dockerfile
│   │   │   └── target
│   │   └── webapp
│   │       ├── Dockerfile
│   │       └── target
│   └── docker-compose.yml
└── k8s
    ├── orderservice.yaml
    ├── paymentservice.yaml
    ├── redis.yaml
    ├── stockservice.yaml
    └── webapp.yaml

/src/main/java/com/example/wdm/
├── DaprApplication.java
├── Exception
│   └── OrderException.java
├── WdmApplication.java
├── order
│   ├── OrderActor.java
│   ├── OrderActorImpl.java
│   ├── OrderActorService.java
│   ├── OrderCallActor.java
│   ├── OrderController.java
│   └── OrderService.java
├── payment
│   ├── PaymentActor.java
│   ├── PaymentActorImpl.java
│   ├── PaymentActorService.java
│   ├── PaymentCallActor.java
│   ├── PaymentController.java
│   └── PaymentService.java
└── stock
    ├── StockActor.java
    ├── StockActorImpl.java
    ├── StockActorService.java
    ├── StockCallActor.java
    ├── StockController.java
    └── StockService.java
```

## Deployment
### run locally
**requirement**: Docker, dapr, JDK 11 or above 
First install dapr locally, see [this](https://docs.dapr.io/getting-started/install-dapr-cli/). 
Then just compile and run following the commands below:
~~~
cd wdm
mvn clean install

dapr run --components-path ./components --app-id orderactorservice --app-port 3000 -- java -jar target/spring-boot-0.0.1-SNAPSHOT.jar com.example.wdm.order.OrderActorService

dapr run --components-path ./components --app-id stockactorservice --app-port 3001 -- java -jar target/spring-boot-0.0.1-SNAPSHOT.jar com.example.wdm.stock.StockActorService

dapr run --components-path ./components --app-id paymentactorservice --app-port 3002 -- java -jar target/spring-boot-0.0.1-SNAPSHOT.jar com.example.wdm.payment.PaymentActorService

dapr run --components-path ./components --app-id wdmservice -- java -jar target/spring-boot-0.0.1-SNAPSHOT.jar com.example.wdm.WdmApplication
~~~
### use docker compose
**requirement**: Docker, docker-compose
~~~
docker-compose build
docker-compose up
~~~

### use k8s
**requirement**: kubectl, a kubernetes cluster
First install dapr, see [this](https://docs.dapr.io/getting-started/install-dapr-cli/).

~~~
# This is for Linux install dapr
wget -q https://raw.githubusercontent.com/dapr/cli/master/install/install.sh -O - | /bin/bash
~~~

Connect your kubernetes cluster. Then follow the commands below to deploy Dapr to it:
~~~
dapr init --kubernetes --wait
dapr status -k
~~~

Create and configure a statestore using redis:

~~~
helm repo add bitnami https://charts.bitnami.com/bitnami
helm repo update
helm install redis bitnami/redis
kubectl apply -f redis.yaml
~~~

Create pods and services:

~~~
kubectl apply -f paymentservice.yaml
kubectl apply -f orderservice.yaml
kubectl apply -f stockservice.yaml
kubectl apply -f webapp.yaml
~~~

Now you can curl locally:

~~~
kubectl port-forward service/webapp 8080:80
curl -X POST 127.0.0.1:8080/payment/create_user
~~~
If you are using a public cloud provider, you can substitue your EXTERNAL-IP address instead of port forwarding. For eample:

~~~
kubectl get services //see the EXTERNAL-IP of webapp
curl -X POST 34.107.103.62:80/payment/create_user //EXTERNAL-IP of webapp + PORT
~~~



### invoke service
**order:**
```
curl -X POST 127.0.0.1:8080/orders/create/{user_id}

curl -X DELETE 127.0.0.1:8080/orders/remove/{order_id}

curl -X GET 127.0.0.1:8080/orders/find/{order_id}

curl -X POST 127.0.0.1:8080/orders/addItem/{order_id}/{item_id}

curl -X DELETE 127.0.0.1:8080/orders/removeItem/{order_id}/{item_id}

curl -X POST 127.0.0.1:8080/orders/checkout/{order_id}
```

**stock:**
```
curl -X GET 127.0.0.1:8080/stock/find/{item_id}

curl -X POST 127.0.0.1:8080/stock/subtract/{item_id}/{number}

curl -X POST 127.0.0.1:8080/stock/add/{item_id}/{number}

curl -X POST 127.0.0.1:8080/stock/item/create/{price}
```


**payment:**
```
curl -X POST 127.0.0.1:8080/payment/create_user

curl -X GET 127.0.0.1:8080/payment/find_user/{user_id}

curl -X POST 127.0.0.1:8080/payment/add_funds/{user_id}/{amount}

curl -X POST 127.0.0.1:8080/payment/pay/{user_id}/{order_id}/{amount}

curl -X GET 127.0.0.1:8080/payment/status/{order_id}

curl -X POST 127.0.0.1:8080/payment/cancel/{user_id}/{order_id}
```
