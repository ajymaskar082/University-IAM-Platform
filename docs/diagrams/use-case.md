
@startuml
left to right direction
skinparam packageStyle rectangle
skinparam shadowing false
skinparam actorStyle awesome

actor "SUPER_ADMIN" as SA
actor "ADMIN" as A
actor "STUDENT" as ST
actor "TEACHER" as T
actor "DEAN" as D
actor "HR_MANAGER" as HR
actor "SECURITY_OFFICER" as SO

rectangle "University IAM Platform" {

  usecase "Register User" as UC1
  usecase "Login" as UC2
  usecase "Validate Credentials" as UC3
  usecase "Verify MFA (TOTP)" as UC4
  usecase "Check Account Status" as UC5
  usecase "Generate JWT Tokens" as UC6
  usecase "Refresh Token" as UC7
  usecase "Logout" as UC8

  usecase "Manage Users" as UC9
  usecase "Assign / Modify / Remove Roles" as UC10
  usecase "Manage Account Status" as UC11
  usecase "Manage Security Policies" as UC12

  usecase "Check RBAC Permissions" as UC13
  usecase "Check ABAC Attributes" as UC14
  usecase "Access Protected Resources" as UC15

  usecase "Manage Profile" as UC16
  usecase "View Learning Information" as UC17
  usecase "Manage Courses and Grades" as UC18
  usecase "Manage Faculty Information" as UC19
  usecase "Manage Employee Accounts" as UC20

  usecase "View Audit Logs" as UC21
  usecase "Monitor Security Events" as UC22
  usecase "Limit Failed Login Attempts" as UC23
  usecase "Revoke Active Sessions and Tokens" as UC24
}

SA --> UC1
SA --> UC2
SA --> UC9
SA --> UC10
SA --> UC11
SA --> UC12
SA --> UC21

A --> UC1
A --> UC2
A --> UC9
A --> UC10
A --> UC11

ST --> UC2
ST --> UC8
ST --> UC16
ST --> UC17

T --> UC2
T --> UC8
T --> UC16
T --> UC18

D --> UC2
D --> UC8
D --> UC16
D --> UC19

HR --> UC2
HR --> UC8
HR --> UC20

SO --> UC2
SO --> UC21
SO --> UC22
SO --> UC23

UC2 ..> UC3 : <<include>>
UC2 ..> UC5 : <<include>>
UC2 ..> UC6 : <<include>>
UC4 ..> UC2 : <<extend>>
UC8 ..> UC24 : <<include>>
UC15 ..> UC13 : <<include>>
UC15 ..> UC14 : <<include>>
UC11 ..> UC24 : <<include>>

UC2 ..> UC23 : <<include>>

@enduml
