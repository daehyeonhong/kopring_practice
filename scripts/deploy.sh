#!/bin/bash

cd /home/ec2-user/app

DOCKER_APP_NAME=spring

EXIST_BLUE=$(sudo docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml ps | grep Up)
echo "배포 시작일자 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >>/home/ec2-user/deploy.log

if [ -z "$EXIST_BLUE" ]; then

  echo "blue 배포 시작 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >>/home/ec2-user/deploy.log

  sudo docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml up -d --build

  sleep 30

  sudo docker network connect spring-network ${DOCKER_APP_NAME}-blue
  echo "green 중단 시작 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >>/home/ec2-user/deploy.log

  sudo docker-compose -p ${DOCKER_APP_NAME}-green -f docker-compose.green.yml down

  sudo docker image prune -af

  echo "green 중단 완료 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >>/home/ec2-user/deploy.log

else
  echo "green 배포 시작 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >>/home/ec2-user/deploy.log
  sudo docker-compose -p ${DOCKER_APP_NAME}-green -f docker-compose.green.yml up -d --build

  sleep 30

  sudo docker network connect spring-network ${DOCKER_APP_NAME}-green
  echo "blue 중단 시작 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >>/home/ec2-user/deploy.log
  sudo docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml down
  sudo docker image prune -af

  echo "blue 중단 완료 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >>/home/ec2-user/deploy.log

fi
echo "배포 종료  : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >>/home/ec2-user/deploy.log
echo sudo service nginx reload >>/home/ec2-user/deploy.log
echo "===================== 배포 완료 =====================" >>/home/ec2-user/deploy.log
echo >>home/ec2-user/deploy.log
