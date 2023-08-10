# Java EDD Example

```
make push
make build
SERVICE=producer make compile
SERVICE=consumer1 make compile
SERVICE=consumer2 make compile
make up
SERVICE=consumer1 make logs
SERVICE=consumer2 make logs
```

On http://localhost:8080/ you can see a kafka control panel, go on kafka cluster -> topics and click on the topic you want to inspect, then go on Messages and look at each messages.
You can go on Consumers to see the consuming progression of each consumer in each consumer group.