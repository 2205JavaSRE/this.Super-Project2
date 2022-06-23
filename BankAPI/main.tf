# Terraform CLI:
#     To create a Terraform file, we can create a file with a .tf
#     To start "executing" terraform stuff, we need to initialize an environment where the terraform file is:
#     (the .tf does not need to be in the same folder as the executable)
#     (the .tf must contain a provider, e.g. AWS, GCP, ... etc )
#         $terraform init

#     We can check to see what our terraform file will do by:
#         $terraform plan

#     We can execute it:
#         $terraform apply
#         $terraform apply --auto-approve

#     We can destroy resources that we created by either commenting out or removing the
#     resource declaration and rexecuting the file or we can execute:
#         $terraform destroy

# terraform.tfstate: will keep track of all the resources it creates, so it knows what to modify, create or delete.
#                     IT IS VERY IMPORTANT that you don't change or delete anything.

#----------------------------------------------------------------------------------------------

#1) Configure the AWS provider so that terraform has access!
#
#provider "<provider, e.g. AWS, GCP>" {
#     key=value
#     access_key=
#     secret_key=
# }

provider "aws" {
  region = "us-east-1"
  access_key="AKIA4OORU3V5X3OS7XPL"
  secret_key = "pe1zaYl4NdItMlPvfbUNWwfEd97nSs+emg9pb80+"
}


#2) Provision resources!

# resource "<provider>_<resource_type>" "<name>"{
#     config options...
#     key = "value"
#     key2 = "another value of stuff"
# }

#Setting up security group

resource "aws_security_group" "terra_ec2_access" {
  name = "allow access to ec2"
  description = "allow access to db and ec2"

#Defining inbound rules for our services
  ingress { #jenkins port
      from_port = 8080
      to_port = 8080
      protocol = "tcp"
      cidr_blocks = ["0.0.0.0/0"]
      ipv6_cidr_blocks = ["::/0"]
  }

  ingress {
      from_port = 22
      to_port = 22
      protocol = "tcp"
      cidr_blocks = ["0.0.0.0/0"]
      ipv6_cidr_blocks = ["::/0"]
  }

#Egress rules are for defining outbound rules!
  egress {
      from_port = 0
      to_port = 0
      protocol = "-1"
      cidr_blocks = ["0.0.0.0/0"]
      ipv6_cidr_blocks = ["::/0"]
  }


}

#Provisiong a RDS database
# resource "aws_db_instance" "terraform_db_instance" {
#     allocated_storage = 20
#     engine = "postgres"
#     engine_version = "14.1"
#     instance_class = "db.t3.micro"
#     username = "postgres"
#     password = "password"
#     skip_final_snapshot = true
#     publicly_accessible = true

# #Associate "allow_db_access" to our db instance.
#     vpc_security_group_ids = [aws_security_group.allow_db_access_terra.id ]

#     tags = {
#       name = "terraform db"
#       purpose = "demo"
#       owner = "ben"
#     }
# }

#Provisioning a EC2 instance
resource "aws_instance" "jenkins_web_server" {

    ami = "ami-0cff7528ff583bf9a"
    instance_type = "t2.micro"
    availability_zone = "us-east-1a"
    key_name = "2205RevatureRSA"

    vpc_security_group_ids = [aws_security_group.terra_ec2_access.id]

    user_data = <<-EOF
                #!/bin/bash
                sudo yum update -y
                sudo yum install git -y
				sudo yum install -y docker
				sudo service docker start
				sudo usermod -a -G docker ec2-user
				curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
				sudo install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl
				sudo wget -O /etc/yum.repos.d/jenkins.repo https://pkg.jenkins.io/redhat-stable/jenkins.repo
				sudo rpm --import https://pkg.jenkins.io/redhat-stable/jenkins.io.key
				sudo yum upgrade
				sudo yum install jenkins java-1.8.0-openjdk-devel -y
				sudo systemctl daemon-reload
				sudo systemctl start jenkins
				sudo systemctl status jenkins
                EOF

    tags = {
      "name" = "jenkins-server"
    }
}