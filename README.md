## Microservice specially designed for Docker, Kubernetes and Istio testing

---
### Endpoints:
* ```/```: Hello world message from ```hostname``` + invocation counter
* ```/healthz/ready```: Liveness probe
* ```/misbehave```: Emulates liveness to return HTTP Code 503
* ```/behave```: Emulates liveness to return HTTP Code 200
* ```/callanother?url=```: Call an endpoint from this component (i.e. container)
* ```/sysresources```: Used memory, Max memory and Cores

---
### Building:

```bash
$ ./mvnw package
$ docker build -t podtesterspring:v2 .
```
---
## Run:
```bash
$ docker run -p 8080:8080 podtesterspring:v2
```

```bash
$ curl -w "\n" -i localhost:8080/
$ curl -w "\n" -i localhost:8080/healthz/ready
```

