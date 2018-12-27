# StarkHub
     Peer-to-peer Streaming Software using RTSP protocol .

#### How it works : <br />
     1. A central server is used for user authentication . 
     2. Central server sends information of newly connected nodes to each already connected nodes .
     3. Nodes can contact each other with search queries (query for searching a video).
     4. Source node then start streaming of video and destination node will enjoy the video .
     5. Every node is holding its own database (information about path of videos).
     6. RTSP protocol is used for streaming .
     7. Each node can maintain number of channels, upload videos to channels, can track subscribers, .
     8. Each user can also like videos, subscribe to channels .
     
#### How to use : <br />
     1. Just download PeerClient_2 folder and run Peer.java file and rest instructions will be there in application itself .
     
#### Technology used : 
     1. JAVA Application build on NETBEANS .
     2. Central server is concurrent server(can handle multiple connections simultaneously) .
     3. MySQL database at central server to store user information .
     4. MySQL database at each node to maintain information about its videos .
     5. VLCJ library to do streaming using VLC media player .
     
#### New features to be implemented :
     1. Efficient searching of videos. 
     2. less number of edges in entire network(by connecting few nodes and transmitting query packets with TTL value) .
     3. Efficient database at each node . 
