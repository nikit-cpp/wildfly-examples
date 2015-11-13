Установка в standalone - очевидно,
в domain - не очевидно, надо добавить в тот профиль, с которым работает сервер-группа, в которую будет задеплоен этот варик
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
http://127.0.0.1:8080/wildfly-pooled-tasks-1.0/async/3000
http://127.0.0.1:8080/wildfly-pooled-tasks-1.0/sync/3000

# это так можно мониторить только в standalone, в domain не получилось
/subsystem=ejb3/thread-pool=default:read-attribute(name=current-thread-count)
/subsystem=ejb3/thread-pool=nikita_pool:read-attribute(name=current-thread-count)
/subsystem=ejb3/thread-pool=default:read-attribute(name=queue-size)
/subsystem=ejb3/thread-pool=default:read-resource(include-runtime=true)