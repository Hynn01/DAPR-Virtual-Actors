# DAPR-Virtual-Actors

cd wdm

mvn clean install


% run this command first:

dapr run --components-path ./components/actors --app-id demoactorservice --app-port 3000 -- java -jar target/spring-boot-0.0.1-SNAPSHOT.jar com.example.wdm.demo.DemoActorService -p 3000

% following:

dapr run --components-path ./components/actors --app-id demoactorclient -- java -jar target/spring-boot-0.0.1-SNAPSHOT.jar com.example.wdm.demo.DemoActorClient

---------------------------
what you supposed to see:
server side:
print("service:create user")

client side
print("client:create user")