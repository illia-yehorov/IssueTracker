Добавить Ingress:

minikube addons enable ingress
minikube addons enable ingress-dns  # не уверен, что нужен

Добавить в c:\Windows\System32\Drivers\etc\hosts:

127.0.0.1 tracker.local
127.0.0.1 grafana.local
127.0.0.1 prometheus.local

Запустить команду:

minikube tunnel