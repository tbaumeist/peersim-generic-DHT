# peersim-generic-DHT
Generic DHT anonymous protocol simulator build on the PeerSim framework.

Main class: peersim.Simulator
Arguments: example.cfg
Class Path: add contents of /lib


## Notes

DHTProtocol
 - topology - linkable peer connection. Topology
 - transport - connections used for communications
 - address = node location
 - routing_table - How the protocol lookups up possible routing paths
 - router - processes messages and routes to next node
   - loop_detection = how loops are detected
   - can_backtrack
   - route_store_file

TODO
 - Add topology generation controllers
    - Random topologies - have maybe
    - Small world - need
    - Structured - need
 - Churn
 - Random message failure
 - anonymity metric - hard
