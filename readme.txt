Установка
        <subsystem xmlns="urn:jboss:domain:ee:2.0">
            <spec-descriptor-property-replacement>false</spec-descriptor-property-replacement>
            <concurrent>
                <context-services>
                    <context-service name="default" jndi-name="java:jboss/ee/concurrency/context/default" use-transaction-setup-provider="true"/>
                </context-services>
                <managed-thread-factories>
                    <managed-thread-factory name="default" jndi-name="java:jboss/ee/concurrency/factory/default" context-service="default"/>
                </managed-thread-factories>
                <managed-executor-services>
                    <managed-executor-service name="default" jndi-name="java:jboss/ee/concurrency/executor/default" context-service="default" hung-task-threshold="60000" core-threads="5" max-threads="25" keepalive-time="5000"/>
добавить ->         <managed-executor-service name="nikita_2_executor" jndi-name="java:jboss/ee/concurrency/executor/nikita_2_executor" long-running-tasks="true" core-threads="10" max-threads="100" queue-length="3000"/>



GET
http://127.0.0.1:8080/fuck-1.0/