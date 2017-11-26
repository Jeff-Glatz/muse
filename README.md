# Muse Workbench
This project integrates Spring Boot and JavaFX to serve as a foundation for
my experiments with the Muse headband on the standard Java platform. There
is no Muse SDK for the standard Java platform, so this project requires the
presence of an OSC server producing muse messages, i.e. 
[MuseIO](http://developer.choosemuse.com/research-tools/museio)  

This project consists of the following
modules:

| module | purpose |
|--------|---------|
|`model`|muse workbench domain objects| 
|`osc`|a framework for processing muse OSC messages|
|`platform`|an experiment in direct headband connection|
|`ui`|the Spring Boot/JavaFX application for exploring the Muse headband|

# Running Muse Workbench
To run the application follow the steps below.

## Build the project
This is a maven multi-module project, so first build all the modules used by the application.
From the root of the project:
```
mvn clean install
```

## Launch the application
Navigate to the `ui` module
```
cd ui
```

Launch the application
```
mvn spring-boot:run
```

This will launch the application with the `simulation` profile activated.

### Runtime configuration properties
By default, Muse Workbench will listen for incoming OSC messages on `tcp://0.0.0.0:5000` which
is the default used by MuseIO. To control certain aspects of Muse Workbench, the following
configuration properties are available:

|System Property|Values|Default Value|
|---|---|---|
|`muse.osc.protocol`|`udp`,`tcp`|`tcp`|
|`muse.osc.receiver.host`| |`0.0.0.0`|
|`muse.osc.receiver.port`| |`5000`|
|`muse.osc.transmitter.host`| |`localhost`|
|`muse.osc.transmitter.port`| |`5000`|

To run with a customized system property:
```
mvn spring-boot:run -D<name>=<value> -D<name>=<value> ...
```

To run with defined profiles:
```
mvn spring-boot:run -Dspring.profiles.active=<profiles>
```

### Muse Headband Simulator
When the `simulator` profile is active, 2 JMX objects will be registered with the runtime
that simulates the presence of a Muse headset:

* `kungzhi.muse.osc.simulation.MuseSimulator`
* `kungzhi.muse.osc.simulation.BandPowerSimulator`

#### Accessing the simulator objects
To connect to the local JVM's MBean server, launch `jconsole` and attach to the process that
looks something like: 
```
org.codehaus.plexus.classworlds.launcher.Launcher spring-boot:run
```

#### Things to keep in mind
Muse Workbench is event driven and centered around the primary `kungzhi.muse.model.Configuration`
object. Because headsets can have different sensor layouts, Muse Workbench waits until a 
configuration message arrives over OSC before activating certain UI features. 

Because of this, the first thing to do when running the simulator is to use the 
`kungzhi.muse.osc.simulation.MuseSimulator` MBean to send a configuration message. After that, the
`kungzhi.muse.osc.simulation.BandPowerSimulator` MBean can be used to send random band power data.

Don't forget to turn on the OSC client in Muse Workbench in order to start receiving the stream of 
OSC muse messages.