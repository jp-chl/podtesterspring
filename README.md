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
$ docker build -t bx-ms-podtester:v1 .
```
---
## Run:
```bash
$ docker run -p 8080:8080 bx-ms-podtester:v1
```

```bash
$ curl -w "\n" -i localhost:8080/
$ curl -w "\n" -i localhost:8080/healthz/ready
```

