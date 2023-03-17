# curso-kubernetes
curso-kubernetes

## Comandos Docker
### docker build -t msvc-users . -f .\msvc-users\Dockerfile
Creamos imagen con  el código modificado

### docker tag msvc-users saraccortes/msvc-users:v1
Creamos etiqueta para subirlo a hithub docker

## Comandos Kubectl
### Kubernetes con Administrador
#### minikube delete
#### minikube start --driver=hyperv
#### minikube status

### kubectl create deployment msvc-users-db --image=mysql:8 --port=3306
Crea objeto deployment para crear una imagen a partir de una imagen previa y se le asigna un puerto
La forma imperativa  no permite pasar variables de ambiente al contenedor.

### kubectl create deployment msvc-users-db --image=mysql:8 --port=3306 --dry-run=client -o yaml > deployment-msvc-users-db-mysql.yaml
Crea un archivo de configuración de forma declarativa. Una vez introducidas las variables de entorno con la  etiqueta  env
- env:
- name: MYSQL_ROOT_PASSWORD
value: sasa
- name: MYSQL_DATABASE
value: msvc_users

lo aplicamos con la sentencia 
- kubectl apply -f .\deployment-msvc-users-db-mysql.yaml

### kubectl get deployments
Obtiene los deployments
También se puede hacer con kubectl get deploy

### kubectl get pods
Obtiene los pods

### kubectl describe pods msvc-users-db-68449dcf49-qvnth
Describe los eventos de un pod indicado

### kubectl logs msvc-users-db-68449dcf49-qvnth
Vemos los logs

### kubectl delete deployment msvc-users-db
Elimina el deployment de forma imperativa

### kubectl delete -f .\deployment-msvc-users.yaml
Elimina el deployment configurado en el archivo de forma declarativa

### kubectl expose deployment msvc-users-db --port=3306 --type=ClusterIP
Habilitar puerto del contenedor mysql que hemos creado

### kubectl expose deployment msvc-users-db --port=3306 --type=NodePort
Permite que los usuarios se puedan conectar a nuestros servicios.
Hay que tener en cuenta que si tenemos muchas instancias no podemos utilizar un puerto específico. 
En este caso necesitariamos un loadbalancer.
- kubectl expose deployment msvc-users-db --port=3306 --type=LoadBalancer

### kubectl get services
Visualizamos los servicios.
De forma abreviada sería
- kubectl get svc

### kubectl describe service msvc-users-db
Vemos características de nuestro servicio

### kubectl get all
Vemos todo lo  relacionado  a nuestro deployment


## Creando servicio msvc-users
### kubectl create deployment msvc-users --image=saraccortes/msvc-users:v1 --port=8001
Creamos  el deployment o imagen y exponemos el puerto con tipo LoadBalancer
- kubectl expose deployment msvc-users --port=8001 --type=LoadBalancer

### minikube service msvc-users --url
Obtiene la ruta pública

### kubectl set image deployment msvc-users msvc-users=saraccortes/msvc-users:v2
Cambiar imagen del contenedor de forma imperativa

### kubectl scale deployment msvc-users --replicas=3
Se crean replicas del contenedor de forma imperativa
 ms
### kubectl get service msvc-users-db -o yaml
Muestra información en formato yaml

### kubectl get service msvc-users-db -o yaml > svc-msvc-users-db.yaml
Genera fichero yaml

### kubectl get service msvc-users -o yaml > svc-msvc-users.yaml
Creamos el servicio de msvc-users

### kubectl apply -f .\deployment-msvc-users.yaml
Aplicar deployment msvc-users

### kubectl get sc
Obtiene  clase de almacenamiento



