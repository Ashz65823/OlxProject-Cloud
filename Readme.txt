Sequence in which we should run micro services									Port No

1.	Run Eureka Server(Keep it running forever) 				Eureka Server			8761
2.	Run Config Server(Keep it running) 					Eureka client  			8888
		-Behind the scean we can change property values(Typically git hub) 	Eureka client
3.	Run ApiGateWay Server(Keep it running) 					Eureka client			9090
4.	Zipkin Server(Keep it running)										9411
--Without this 4 steps don't run your service---------

5.	Olx-User								Eureka client			9051
6. 	Olx-Masters							Eureka client			9052	
7. 	Olx-Advertisement							Eureka client			9053

---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
