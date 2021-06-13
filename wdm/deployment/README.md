### Kubernetes

install dapr

~~~
wget -q https://raw.githubusercontent.com/dapr/cli/master/install/install.sh -O - | /bin/bash
~~~

after connect cluster

~~~
dapr init --kubernetes --wait
dapr status -k
~~~

redis:

~~~
helm repo add bitnami https://charts.bitnami.com/bitnami
helm repo update
helm install redis bitnami/redis
kubectl apply -f redis.yaml
~~~

pods

~~~
kubectl apply -f paymentservice.yaml
kubectl apply -f orderservice.yaml
kubectl apply -f stockservice.yaml
kubectl apply -f webapp.yaml
~~~

curl locally:

~~~
kubectl port-forward service/webapp 8080:80
curl -X POST 127.0.0.1:8080/payment/create_user
~~~

external port:

~~~
kubectl get services //see the EXTERNAL-IP of webapp
curl -X POST 34.107.103.62:80/payment/create_user //EXTERNAL-IP of webapp + PORT
~~~

