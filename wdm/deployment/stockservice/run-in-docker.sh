#!/bin/sh

echo "********************************************************"
echo "StockActorService start"
echo "********************************************************"
#dapr run --components-path ./components/actors --app-id demoactorservice --app-port 3000 -- java -jar target/spring-boot-0.0.1-SNAPSHOT.jar com.example.wdm.order.OrderActorService -p 3000

java -jar /app.jar com.example.wdm.stock.StockActorService
