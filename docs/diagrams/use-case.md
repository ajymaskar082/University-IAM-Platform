
# Use Case Diagram — University IAM Platform

## Actors
- SUPER_ADMIN
- ADMIN
- STUDENT
- TEACHER
- DEAN
- HR_MANAGER
- SECURITY_OFFICER

## Use Case Diagram

```mermaid
flowchart LR
    SA["SUPER_ADMIN"]
    AD["ADMIN"]
    ST["STUDENT"]
    TE["TEACHER"]
    DE["DEAN"]
    HR["HR_MANAGER"]
    SO["SECURITY_OFFICER"]

    subgraph IAM["University IAM Platform"]
        UC1(["Login"])
        UC2(["Register"])
        UC3(["MFA Verification"])
        UC4(["Change Password"])
        UC5(["View Profile"])
        UC6(["Manage Users"])
        UC7(["Assign Roles"])
        UC8(["Manage Permissions"])
        UC9(["Lock / Unlock Account"])
        UC10(["Manage Students and Teachers"])
        UC11(["Manage Employees"])
        UC12(["Review Access Requests"])
        UC13(["View Audit Logs"])
        UC14(["Monitor Security Events"])
        UC15(["Check Access Permissions"])
    end

    SA --> UC1
    SA --> UC3
    SA --> UC6
    SA --> UC7
    SA --> UC8
    SA --> UC9
    SA --> UC13
    SA --> UC15

    AD --> UC1
    AD --> UC3
    AD --> UC6
    AD --> UC7
    AD --> UC9
    AD --> UC13
    AD --> UC15

    ST --> UC1
    ST --> UC2
    ST --> UC3
    ST --> UC4
    ST --> UC5
    ST --> UC15

    TE --> UC1
    TE --> UC3
    TE --> UC4
    TE --> UC5
    TE --> UC15

    DE --> UC1
    DE --> UC3
    DE --> UC5
    DE --> UC10
    DE --> UC12
    DE --> UC15

    HR --> UC1
    HR --> UC3
    HR --> UC5
    HR --> UC11
    HR --> UC12
    HR --> UC15

    SO --> UC1
    SO --> UC3
    SO --> UC13
    SO --> UC14
    SO --> UC15
```
