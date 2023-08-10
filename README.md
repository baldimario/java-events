# Java EDD Example
This example runs some java applications that communicate through kafka realizing an event driven microservices mesh.

A producer sends 1 million events in few seconds and the consumers consumes them realtime without skipping a single one of them.

## How to run
```
make pull
make build
SERVICE=producer make compile
SERVICE=consumer1 make compile
SERVICE=consumer2 make compile
make up
SERVICE=producer make logs
SERVICE=consumer1 make logs
SERVICE=consumer2 make logs
```

Let's analyze the chain of commands:
- `make pull` uses docker-compose to pull the images
- `make build` build the custom docker images
- `SERVICE=... make compile` invoke the docker service named $SERVICE and run maven to compile the application
- `make up` it runs dockers through docker-compose in detached mode, an healtcheck for zookeeper will start after 10s delay
- `SERVICE=... make logs` show the logs for the docker container service named $SERVICE

The outcome will be the following one:
- The producer produces events and send them on topic1 and topic2
- The consumer1 listens for events on topic1 and log them
- The consumer2 listens for events on topic2 and log them

## Utilities
On http://localhost:8080/ you can see a kafka control panel, go on kafka cluster -> topics and click on the topic you want to inspect, then go on Messages and look at each messages.
You can go on Consumers to see the consuming progression of each consumer in each consumer group.

## Architecture

This is the microservices mesh created
```
                            ┌─────────┐
                topic1  ┌──►│CONSUMER1│
              ┌─────────┘   └─────────┘
 ┌────────┐   │
 │PRODUCER├───┤
 └────────┘   │
              └─────────┐   ┌─────────┐
                topic2  └──►│CONSUMER2│
                            └─────────┘
```
The producer produce some events and decides to send some of them on topic1 and others on topic2
The first consumer consume the events on the first queue
The second consumer consume the other events

## Furthermore
Nothing excludes more complex event driven configurations to devleop even further the data flow, eg.:
```
                            ┌─────────┐
                topic1  ┌──►│CONSUMER1│
              ┌─────────┘   └─────────┘
 ┌────────┐   │
 │PRODUCER├───┤
 └────────┘   │
              ├─────────┐   ┌─────────┐ topic4 ┌─────────┐
              │ topic2  └──►│CONSUMER2├───────►│CONSUMER3│
              │             └─────────┘        └─────────┘
              │                  ▲
              │ topic3           │
              └──────────────────┘
```
Where a producer sends events to 3 topics, two of them are intercepted from consumer2 that sends other events (maybe the received ones but processed) to consumer3 on another topic.
The producer sends other events to consumer1 on another topic.


Or even further:
```

                             ┌─────────┐
                         ┌──►│CONSUMER1│
                         │   └─────────┘
 ┌─────────┐    topic1   │
 │PRODUCER1├─────────────┤
 └─────────┘             │
                         │   ┌─────────┐ topic4 ┌─────────┐
                         └──►│CONSUMER2├───────►│CONSUMER4│
                             └─────────┘        └─────────┘
 ┌─────────┐    topic2           ▲                   ▲
 │PRODUCER2├───────────┬─────────┘                   │
 └─────────┘           │                             │
                       │                             │
                       │                             │
                       ▼                             │
              ┌─────────────────┐        topic3      │
              │PRODUCER/CONSUMER├────────────────────┘
              └─────────────────┘
```
Here we see two producers send events to the consumers.
Some consumers listen for events on the same topic:
- they can split messages balancig them if they are in the same consumer group
- or read all them not interfering each other if they are in two separated consumer group
A consumer is also producer, this case is very common when you catch suspicious events and want to notify sending the event with other details to a noitication service that will send some notificaion/email.
