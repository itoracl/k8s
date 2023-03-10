# k8s
K8S Cluster Bootstrap masters all

- Разработал CI процесс раскатки кластера kubernetes на личных компьютерах, пайплайн jenkins выполняется менее 3 минут. Раскатку написал под самую последнюю версию 1.26, там много нового в самом кубере, в том числе касается инсталляции. Результаты работы пайплайна можно посмотреть здесь: https://github.com/itoracl/k8s/blob/f68072d535d434910647d14dad551fa5847890d5/jenkins%20-%20K8S%20Cluster%20Bootstrap%20masters%20all%20-%20%2375%203.pdf
И в чистом jenkins там же, плюс еще несколько деплойментов.
  Как видно, используется 4 параллельных stage на хосте jenkins, ansible хост настроен на выполнение 50 одновременных соединений. На основе официальной документации kubernetes.io и методологии The Hard Way, было что проанализировать и оптимизировать в плане исполнения.
  Оперативное решение для развертывания полнофункиональной среды тестирования за короткое время. (Некоторые корпорации шли к этому годами)
Некоторые технические (красивые) отчеты по моему кластеру: https://github.com/itoracl/k8s/blob/f68072d535d434910647d14dad551fa5847890d5/CEMEA-NORTH-MOSCOW-ITORACLE%20-%20Kubernetes%20Dashboard%2010.pdf
Всех тонкостей не раскрываю, минимальный совет: после получения артифактов для разворачивания или копирования может показаться логичным учесть это и готовые разворачивать на всех узлах оттуда. Это ошибочное решение в плане производительности: мастер узел это не файловый сервер. А когда там уже стартует etcd и потом все может развалиться? Нагрузка должна отдаваться на сетевую инфраструктуру, можно использовать и внутреннее зеркало для файлового обмена.
- На ubuntu 22.04.1 LTS, Red Hat EE настроил и запустил свой частный кластер на bare metal on premises, без помощи каких-либо провайдеров, включая необходимую конфигурацию локальных маршрутизаторов
- Flannel протестированное решение network overlay для выделения сетевых адресов 
- Grafana service + Prometheus + NodeExporter сервер статистики кластера и СУБД корпоративного уровня, запуститил и настроил самостоятельно
- установил k8s dashboard и metrics server, поднял metrics API
- Helm могу сгенерировать чарты, осуществлять деплойменты под управлением helm. 
- разработал собственные Ansible плейбуки конфигурации кластера, деплойментов, сервисов, ingress-nginx контроллера и ingress класса. То есть все настраивается в ansible через модули kubernetes.core и устанавливается через helm, чтобы можно было оперативно все развернуть за короткое время
- Jenkins могу разрабатывать пайплайны, знаком с canary deployment
- для среды разработки и серверов БД развернул и настроил nfs сервер, чтобы небольшие серверы БД с persistent volume, объявленном через nfs могли назначаться на любом узле кластера. Ясно, в корпоративной среде лучше по другому, ну там и техники больше
- осуществил деплоймент сервера СУБД Postrgesql с настройкой проброса tcp через ingress service configmap на уровень обычной сети
- все тоже самое сделал с MSSQL сервер
- инстанс Oracle RAC (Enterprise manager, APEX со встроенным веб сервером внутри) работает и привязан пока к одному узлу кластера
В вопросах управления кластером отдаю предпочтение построению управляемой (managed), быстровоспроизводимой, надежной среды, планированию отказоустойчивости. Управляет кластером у меня jenkins + ansible, самое главное полностью пересмотреть подход к линейной логике командной строки с учетом алгоритмов CI, не повторять чужие ошибки вида “bashsible”.
Наполнение инструментами разработки в микросервисной архитектуре я считаю не первостепенным вопросом, это все-же не цель условного бизнеса, да и их разнообразие не позволяет все охватить. В общем тут мне кажется уместным иметь дополнительные агенты jenkins даже на автоуправлении по требованию с библиотеками разработки, podman in k8s контейнеры. Докер на данном этапе вообще не использую (флант от него полностью отказался), просто kubectl run … и поехали. Для консервов podman.
Да, уделяю внимание разворачиванию приватных репозиториев git, реестров кода как для ускорения загрузки, так и для моделирования закрытого контура.
