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
добавить ->         <managed-executor-service name="nikita_2_executor" jndi-name="java:jboss/ee/concurrency/executor/nikita_2_executor" long-running-tasks="true" core-threads="1000" max-threads="1000" queue-length="3000"/>


GET
http://127.0.0.1:8080/wildfly-examples-1.0/rest/async/4000
http://127.0.0.1:8080/wildfly-examples-1.0/rest/sync/4000

# это так можно мониторить только в standalone, в domain не получилось
/subsystem=ejb3/thread-pool=default:read-attribute(name=current-thread-count)
/subsystem=ejb3/thread-pool=nikita_pool:read-attribute(name=current-thread-count)
/subsystem=ejb3/thread-pool=default:read-attribute(name=queue-size)
/subsystem=ejb3/thread-pool=default:read-resource(include-runtime=true)

# Посмотреть .html
http://0.0.0.0:8080/wildfly-examples-1.0/www/index.html


Добавить почту
1)		<mail-session name="mySession" jndi-name="java:/MyOtherMail">
			<smtp-server outbound-socket-binding-ref="mail-smtp-yandex" ssl="true" username="login.on.yandex" password="password.on.yandex"/>
		</mail-session>

2)      <outbound-socket-binding name="mail-smtp-yandex">
            <remote-destination host="smtp.yandex.ru" port="465"/>
        </outbound-socket-binding>

Установить кодировку для jsf (https://issues.jboss.org/browse/WFLY-2533):
<servlet-container name="default" default-encoding="UTF-8">

Cargo-плагин
============
https://github.com/bmuschko/gradle-cargo-plugin/issues/24 http://habrahabr.ru/sandbox/69926/  https://github.com/bmuschko/gradle-cargo-plugin

gradle cargoDeployRemote
gradle cargoUndeployRemote
gradle cargoRedeployRemote

bower install

curl -H "Content-Type: application/json" -X POST -d '{"from":"i.familia1@yandex.ru","to":"nikit-xxx@mail.ru", "subject":"Сабж", "body":"Боди"}' http://127.0.0.1:8080/wildfly-examples-1.0/rest/mail/send

curl http://127.0.0.1:8080/wildfly-examples-1.0/rest/mail/getmail