# DAPR-Virtual-Actors

cd wdm

mvn clean install


dapr run --components-path ./components --app-id orderactorservice --app-port 3000 -- java -jar target/spring-boot-0.0.1-SNAPSHOT.jar com.example.wdm.order.OrderActorService


dapr run --components-path ./components --app-id stockactorservice --app-port 3001 -- java -jar target/spring-boot-0.0.1-SNAPSHOT.jar com.example.wdm.stock.StockActorService


dapr run --components-path ./components --app-id paymentactorservice --app-port 3002 -- java -jar target/spring-boot-0.0.1-SNAPSHOT.jar com.example.wdm.payment.PaymentActorService


dapr run --components-path ./components --app-id wdmservice -- java -jar target/spring-boot-0.0.1-SNAPSHOT.jar com.example.wdm.WdmApplication


-----------------------------------------------------
payment:

curl -X POST 127.0.0.1:8080/payment/create_user

curl -X GET 127.0.0.1:8080/payment/find_user/{user_id}

curl -X POST 127.0.0.1:8080/payment/add_funds/{user_id}/{amount}

curl -X POST 127.0.0.1:8080/payment/pay/{user_id}/{order_id}/{amount}

curl -X GET 127.0.0.1:8080/payment/status/{order_id}

curl -X POST 127.0.0.1:8080/payment/cancel/{user_id}/{order_id}

----------------------------------------------------------

Using this two commands, it's able to be deployed on google cloud now!

docker-compose build

docker-compose up

