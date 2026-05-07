# Border Entry Management System (BEMS) 🛂

**A comprehensive Java-based system for managing border entries, visitor information, visa tracking, and automated notifications.**

---

## 📋 Table of Contents

1. [Overview](#overview)
2. [Key Features](#key-features)
3. [Project Structure](#project-structure)
4. [System Architecture](#system-architecture)
5. [Getting Started](#getting-started)
6. [Usage Guide](#usage-guide)
7. [Core Components](#core-components)
8. [Notification System](#notification-system)
9. [Data Management](#data-management)
10. [Best Practices](#best-practices)
11. [Configuration](#configuration)
12. [FAQ](#faq)
13. [Troubleshooting](#troubleshooting)

---

## Overview

The **Border Entry Management System (BEMS)** is an enterprise-grade Java application designed for Border Security Forces (BSF) to efficiently manage and track:

- **Visitor Management**: Registration and tracking of border visitors
- **Visa Administration**: Application processing, approval, and validity tracking
- **Border Entry Records**: Real-time logging of entry/exit activities
- **Automated Notifications**: Email alerts for critical events (visa expiry, overstays, warrants)
- **Compliance Monitoring**: Automatic detection of overstays and warrant issuance

The system operates with **background daemon threads** for continuous monitoring, ensuring no visa expirations or overstays are missed.

### Key Metrics
- ✅ **Zero Compilation Errors** - Production-ready code
- 🔒 **Thread-Safe Operations** - Synchronized data structures
- 📧 **Email Notifications** - SMTP-integrated alerts
- 📊 **Excel-Based Storage** - Easy data persistence and reporting
- ⚡ **Real-time Monitoring** - Daemon threads for continuous checks

---

## Key Features

### 👥 Visitor Management
- Register new visitors with complete profile information
- Track visitor entry and exit dates
- View visitor visa history and applications
- Update visitor contact information

### 📝 Visa Administration
- Submit visa applications
- Admin approval/rejection workflow
- Automatic validity period calculation
- Visa activation at border entry
- Visa revocation capabilities

### 🛃 Border Entry Operations
- Record border entry with associated visa
- Approve/deny entry based on visa status
- Log exit confirmations
- Track entry/exit timing and duration

### 🔔 Automated Notifications (15 Types)
The system sends automated email notifications for:

**Application Lifecycle:**
- Visa application submitted
- Visa application approved
- Visa application rejected

**Visa Monitoring:**
- Visa expiry warnings (5-day notice before expiry)
- Visa expired notifications
- Visa revoked alerts

**Border Operations:**
- Visa activated at entry
- Entry approved
- Entry denied
- Exit confirmed

**Compliance & Legal:**
- Overstay warnings
- Warrant created (overstay > 2 days)
- Warrant closed (case resolved)

**System Events:**
- Account registered
- Account login alerts

### ⏰ Background Monitoring
- **Visa Expiry Check**: Every 60 seconds - Monitors all visas for expiry within 5 days
- **Overstay Detection**: Every 15 seconds - Identifies visitors exceeding allowed stay
- **Automatic Warrant Generation**: Creates warrants for overstays > 2 days
- **Duplicate Prevention**: In-memory and Excel-based tracking prevents duplicate notifications

### 👨‍💼 Admin Functions
- User and visitor management
- Visa application processing
- Border entry record management
- Manual notification sending
- System data viewing and reporting

---

## Project Structure

```
EntryManagementSystem/
│
├── src/                                    # Source Code Directory
│   ├── BSFEntryManagementSystem.java       # Main entry point with admin menu
│   ├── BSFFunctions.java                   # Unified facade for all operations
│   │
│   ├── Core Entities/
│   │   ├── User.java                       # Admin/operator user accounts
│   │   ├── Visitor.java                    # Border visitor information
│   │   ├── VisaApplication.java            # Visa application requests
│   │   ├── Visa.java                       # Approved visa records
│   │   ├── BorderEntryRecord.java          # Entry/exit transactions
│   │   ├── Warrant.java                    # Legal warrant records
│   │   └── Notification.java               # Notification objects (15 types)
│   │
│   ├── Notification System/
│   │   ├── NotificationManager.java        # Facade: 14 notification methods
│   │   ├── NotificationSender.java         # Email delivery (SMTP)
│   │   └── NotificationService.java        # Background daemon threads
│   │
│   ├── Data Persistence/
│   │   ├── LoadExcelData.java              # Reads from Excel sheets
│   │   ├── DisplayExcelData.java           # Formatted data display
│   │   ├── UpdateExcelData.java            # Updates Excel records
│   │   └── WriteDataToExcel.java           # Writes new data to Excel
│   │
│   ├── Configuration/
│   │   └── AppConfig.java                  # SMTP credentials & settings
│   │
│   └── Testing/
│       ├── TestMail.java                   # Email configuration testing
│       └── TestPOI.java                    # Apache POI library testing
│
├── lib/                                    # External Libraries
│   ├── poi-*.jar                          # Apache POI (Excel manipulation)
│   ├── poi-ooxml-*.jar                    # POI OOXML support
│   ├── xmlbeans-*.jar                     # XML processing
│   ├── commons-*.jar                      # Apache Commons utilities
│   ├── activation.jar                     # Java Mail activation
│   └── mail.jar                           # Java Mail API
│
├── bin/                                    # Compiled Output
│
├── data.xlsx                               # Main data storage file
│   ├── Sheet 1: Users
│   ├── Sheet 2: Visitors
│   ├── Sheet 3: Visa Applications
│   ├── Sheet 4: Visas
│   ├── Sheet 5: Border Entry Records
│   ├── Sheet 6: Warrants
│   └── Sheet 7: Notifications
│
├── ARCHITECTURE.md                         # Detailed architecture diagrams
├── BEST_PRACTICES.md                       # Implementation patterns
├── IMPLEMENTATION_SUMMARY.md               # Refactoring details
├── INTEGRATION_GUIDE.md                    # Usage examples
└── README.md                               # Quick start guide

```

---

## System Architecture

### High-Level Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│         BORDER ENTRY MANAGEMENT SYSTEM (BSFEntryManagementSystem)│
│                         Main Admin Menu                          │
└──────────────────────────────┬──────────────────────────────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │   BSFFunctions      │
                    │  (Unified Facade)   │
                    └──┬─────┬────┬───┬───┘
                       │     │    │   │
        ┌──────────────┤     │    │   └──────────────┐
        │              │     │    │                  │
        ▼              ▼     ▼    ▼                  ▼
   ┌─────────┐   ┌──────────┐  ┌──────────┐  ┌─────────────┐
   │ Excel   │   │ Notif    │  │ Notif    │  │Border Entry │
   │I/O      │   │Manager   │  │Service   │  │Operations  │
   │Module   │   │(14 msgs) │  │(Daemon)  │  │Module      │
   └─────────┘   └────┬─────┘  └────┬─────┘  └─────────────┘
                      │             │
                      └─────┬───────┘
                            │
                    ┌───────▼────────┐
                    │Notification    │
                    │Sender          │
                    │(SMTP Email)    │
                    └────────────────┘
```

### Background Monitoring System

```
NotificationService (Daemon Thread Executor)
│
├─► visaExpiryLoop() [Thread 1]
│   ├─ Check interval: 60 seconds
│   ├─ Action: Detect visas expiring within 5 days
│   ├─ Notify: NotificationManager.notifyVisaExpiryWarning()
│   └─ Persist: Write to Excel Sheet 7
│
└─► overstayLoop() [Thread 2]
    ├─ Check interval: 15 seconds
    ├─ Actions:
    │  ├─ Detect any overstays
    │  ├─ Send: NotificationManager.notifyOverstayWarning()
    │  ├─ If overstay > 2 days:
    │  │  ├─ Create Warrant object
    │  │  └─ Send: NotificationManager.notifyWarrantCreated()
    │  └─ Persist all data to Excel Sheet 7
    └─ Auto-resolves when visitor exits
```

### Notification Creation Flow

```
EVENT TRIGGERED
    ↓
DUPLICATE CHECK
  ├─ In-memory Set: sentKeys
  └─ Excel Sheet 7: Previous records
    ↓
CREATE NOTIFICATION
  ├─ NotificationManager.<method>()
  ├─ Generate context-specific message
  └─ Create Notification object
    ↓
PERSIST TO EXCEL
  └─ Sheet 7: Notifications
    ↓
SEND EMAIL
  ├─ NotificationSender.sendNotification()
  ├─ SMTP Configuration from AppConfig.java
  └─ Mark emailSent = true
```

---

## Getting Started

### Prerequisites

- **Java 8 or higher** (JDK)
- **IDE**: VS Code with Java Extensions (Extension Pack for Java)
- **Libraries**: All included in `lib/` directory
  - Apache POI (Excel manipulation)
  - Java Mail API (SMTP)
  - Commons utilities

### Installation Steps

1. **Clone the Repository**
   ```bash
   git clone <repository-url>
   cd EntryManagementSystem
   ```

2. **Open in VS Code**
   ```bash
   code .
   ```

3. **Configure Java Project**
   - VS Code will automatically detect the Java project
   - Libraries in `lib/` are automatically added to classpath
   - Check `.vscode/settings.json` if needed

4. **Build the Project**
   - Use VS Code Java Extension or command line:
   ```bash
   javac -cp "lib/*" -d bin src/*.java
   ```

5. **Email Configuration** (Optional but Recommended)
   - Edit `src/AppConfig.java`
   - Update SMTP credentials:
   ```java
   public static final String ADMIN_EMAIL = "your-email@gmail.com";
   public static final String ADMIN_PASSWORD = "your-app-password";
   public static final String SMTP_HOST = "smtp.gmail.com";
   public static final int SMTP_PORT = 587;
   ```

6. **Run the Application**
   - Press `F5` in VS Code or use:
   ```bash
   java -cp "bin:lib/*" BSFEntryManagementSystem
   ```

---

## Usage Guide

### Initial Login

```
Welcome to the BSF Entry Management System

1. Login
2. Register
3. Exit

Select your choice: 1
```

**Default User** (if first run):
- Username: `admin`
- Password: `admin123`

### Admin Menu Options

Once logged in, the admin menu displays:

```
======== ADMIN MENU ========
1. Register New Visitor
2. Submit Visa Application
3. Process Visa Applications
4. Record Border Entry
5. View All Data
6. Update Existing Data
7. Delete Records
8. Send Manual Notification
9. Logout
```

#### **Option 1: Register New Visitor**
- Input visitor name, contact info, nationality
- System assigns unique visitor ID
- Visitor record saved to Excel Sheet 2

#### **Option 2: Submit Visa Application**
- Select existing visitor
- Enter trip purpose and duration
- Application created and stored in Excel Sheet 3
- Requires admin approval (Option 3)

#### **Option 3: Process Visa Applications**
- View pending applications
- Admin can approve or reject
- Approved: Visa record created with calculated expiry date
- System sends automatic email notification
- Rejected: Rejection reason recorded, email notification sent

#### **Option 4: Record Border Entry**
- Select visitor and their visa
- Record entry timestamp
- System verifies visa validity
- Triggers: `VISA_ACTIVATED` notification

#### **Option 5: View All Data**
- Display all visitors
- Display all visas
- Display all entry records
- Display all notifications
- Display all warrants

#### **Option 6: Update Existing Data**
- Update visitor information
- Update visa details
- Modify entry records
- Changes persisted immediately to Excel

#### **Option 7: Delete Records**
- Remove visitor records
- Delete visa applications
- Remove border entry records
- Cascade delete with confirmation

#### **Option 8: Send Manual Notification**
- Select notification type (15 options)
- Choose recipient visitor
- Enter custom message
- Send via email
- Persist to notification log

#### **Option 9: Logout**
- End admin session
- Return to login screen

### Automatic Background Operations

While the system is running, two daemon threads operate continuously:

**Visa Expiry Monitor** (Every 60 seconds):
- ✓ Scans all visa records
- ✓ Identifies expiring within 5 days
- ✓ Sends warning email automatically
- ✓ Records in notification log

**Overstay Monitor** (Every 15 seconds):
- ✓ Calculates stay duration for all entries
- ✓ Detects any overstays
- ✓ Sends overstay notification
- ✓ If > 2 days overstay: Creates and sends warrant notification

---

## Core Components

### 1. **Entity Classes** (Data Models)

#### User.java
```
Properties:
- userId (unique identifier)
- username (login name)
- password (hashed)
- role (ADMIN, OPERATOR, VIEWER)
- email (contact email)
- createdDate (registration date)
```

#### Visitor.java
```
Properties:
- visitorId (unique identifier)
- name (full name)
- nationality (country)
- passport (passport number)
- dateOfBirth (DOB)
- contactPhone (phone number)
- email (email address)
- address (residential address)
- registeredDate (system date)
```

#### VisaApplication.java
```
Properties:
- applicationId (unique)
- visitor (associated Visitor)
- purpose (reason for visit)
- intendedDuration (days requested)
- submissionDate (application date)
- status (PENDING, APPROVED, REJECTED)
- rejectionReason (if rejected)
```

#### Visa.java
```
Properties:
- visaNumber (unique visa ID)
- visitor (associated Visitor)
- application (source application)
- issueDate (issued date)
- expiryDate (calculated: issueDate + intendedDuration)
- status (VALID, EXPIRED, REVOKED)
- isActivated (used at border entry)
```

#### BorderEntryRecord.java
```
Properties:
- entryId (unique)
- visitor (associated Visitor)
- visa (associated Visa)
- entryDate (entry timestamp)
- exitDate (exit timestamp, if exited)
- entryStatus (APPROVED, DENIED, PENDING)
- overstayStatus (NORMAL, OVERSTAY)
```

#### Warrant.java
```
Properties:
- warrantId (unique)
- visitor (associated Visitor)
- issueDate (creation date)
- reason (overstay reason)
- daysOverstay (duration of overstay)
- status (ACTIVE, CLOSED)
- closedDate (when overstay ended)
```

#### Notification.java
```
Properties:
- notificationId (unique)
- recipient (Visitor)
- type (15 enum types)
- message (notification content)
- createdDate (timestamp)
- emailSent (boolean)
- readStatus (boolean)
```

### 2. **Business Logic Classes**

#### BSFFunctions.java (Unified Facade)
```
Core Methods:
- All Load*FromExcel() - Data loading operations
- All Write*ToExcel() - Data persistence
- All Update*InExcel() - Record updates
- sendNotification() - Email dispatch
- Display*() - Formatted data output
```

#### NotificationManager.java (14 Static Methods)
```
Methods:
1. notifyVisaApplicationSubmitted(Visitor, VisaApplication, List)
2. notifyVisaApplicationApproved(Visitor, visaNumber, List)
3. notifyVisaApplicationRejected(Visitor, reason, List)
4. notifyVisaExpiryWarning(Visitor, Visa, List)
5. notifyVisaExpired(Visitor, Visa, List)
6. notifyVisaRevoked(Visitor, Visa, List)
7. notifyVisaActivated(Visitor, Visa, List)
8. notifyEntryApproved(Visitor, BorderEntryRecord, List)
9. notifyEntryDenied(Visitor, reason, List)
10. notifyExitConfirmed(Visitor, BorderEntryRecord, List)
11. notifyOverstayWarning(Visitor, record, used, daysOverdue, List)
12. notifyWarrantCreated(Visitor, Warrant, List)
13. notifyWarrantClosed(Visitor, Warrant, List)
14. notifyAccountRegistered(User, List)
15. notifyLoginAlert(User, timestamp, List)
```

#### NotificationSender.java (Email Delivery)
```
Core Methods:
- sendNotification(Notification) - Main email sender
- buildEmailContent(Notification) - Message formatting
- getAdminCredentials() - SMTP auth
```

#### NotificationService.java (Background Daemon)
```
Threads:
- visaExpiryLoop() - 60s interval
- overstayLoop() - 15s interval
- Synchronized list management
- Per-row error handling
- Shutdown coordination
```

### 3. **Data Persistence Classes**

#### LoadExcelData.java
- `loadVisitorsFromExcel()` - Sheet 2
- `loadVisaApplicationsFromExcel()` - Sheet 3
- `loadVisasFromExcel()` - Sheet 4
- `loadBorderEntryRecordsFromExcel()` - Sheet 5
- `loadWarrantsFromExcel()` - Sheet 6
- `loadNotificationsFromExcel()` - Sheet 7

#### WriteDataToExcel.java
- `writeVisitorToExcel()` - Append visitor to Sheet 2
- `writeVisaApplicationToExcel()` - Append application to Sheet 3
- `writeVisaToExcel()` - Append visa to Sheet 4
- `writeBorderEntryRecordToExcel()` - Append entry to Sheet 5
- `writeWarrantToExcel()` - Append warrant to Sheet 6
- `writeNotificationToExcel()` - Append notification to Sheet 7

#### UpdateExcelData.java
- `updateVisitorInExcel()` - Modify Sheet 2 row
- `updateVisaApplicationInExcel()` - Modify Sheet 3 row
- `updateVisaInExcel()` - Modify Sheet 4 row
- `updateBorderEntryRecordInExcel()` - Modify Sheet 5 row
- `updateWarrantInExcel()` - Modify Sheet 6 row

#### DisplayExcelData.java
- `displayAllVisitors()` - Formatted table output
- `displayAllVisaApplications()` - Formatted table output
- `displayAllVisas()` - Formatted table output
- `displayAllBorderEntryRecords()` - Formatted table output
- `displayAllWarrants()` - Formatted table output
- `displayAllNotifications()` - Formatted table output

---

## Notification System

### 15 Notification Types

The system supports 15 distinct notification types, each with automatic message generation:

#### **Visa Application Lifecycle** (3 types)

| Type | Trigger | Message | Auto-Send |
|------|---------|---------|-----------|
| `VISA_APPLICATION_SUBMITTED` | User submits visa application | "Your visa application (ID: XXX) has been submitted for review." | No |
| `VISA_APPLICATION_APPROVED` | Admin approves application | "Your visa application has been approved. Visa #: XXX, Valid until: YYYY-MM-DD" | Yes |
| `VISA_APPLICATION_REJECTED` | Admin rejects application | "Your visa application has been rejected. Reason: [rejection reason]" | Yes |

#### **Visa Validity & Expiry** (3 types)

| Type | Trigger | Message | Auto-Send |
|------|---------|---------|-----------|
| `VISA_EXPIRY_WARNING` | Visa ≤5 days to expiry | "Your visa (XXX) will expire on YYYY-MM-DD. Please make arrangements." | Yes (60s check) |
| `VISA_EXPIRED` | Visa date passed | "Your visa (XXX) has expired as of YYYY-MM-DD. Please reapply." | Yes |
| `VISA_REVOKED` | Admin revokes visa | "Your visa (XXX) has been revoked by authorities. Reason: [reason]" | Yes |

#### **Border Operations** (4 types)

| Type | Trigger | Message | Auto-Send |
|------|---------|---------|-----------|
| `VISA_ACTIVATED` | Visitor uses visa at entry | "Your visa has been activated for border entry on YYYY-MM-DD." | Yes |
| `ENTRY_APPROVED` | Entry permission granted | "Your border entry request has been approved. Entry timestamp: YYYY-MM-DD HH:MM:SS" | Yes |
| `ENTRY_DENIED` | Entry rejected | "Your border entry request has been denied. Reason: [reason]" | Yes |
| `EXIT_CONFIRMED` | Visitor exits | "Your border exit has been confirmed. Exit timestamp: YYYY-MM-DD HH:MM:SS" | Yes |

#### **Compliance & Legal** (3 types)

| Type | Trigger | Message | Auto-Send |
|------|---------|---------|-----------|
| `OVERSTAY_WARNING` | Any overstay detected | "You have overstayed by [X] days. You must exit immediately." | Yes (15s check) |
| `WARRANT_CREATED` | Overstay > 2 days | "A warrant has been issued for unauthorized overstay of [X] days." | Yes |
| `WARRANT_CLOSED` | Visitor exits after overstay | "Your case has been closed. Warrant closed on YYYY-MM-DD." | Yes |

#### **System & Security** (2 types)

| Type | Trigger | Message | Auto-Send |
|------|---------|---------|-----------|
| `ACCOUNT_REGISTERED` | New user account created | "Your account has been successfully created. Username: [username]" | No |
| `ACCOUNT_LOGIN_ALERT` | Suspicious login | "Alert: New login attempt detected on YYYY-MM-DD HH:MM:SS from IP: [IP]" | No |

### Duplicate Prevention Mechanism

The system prevents duplicate notifications through multi-layer checking:

```
┌─ Notification Trigger Event
└─► Check 1: In-Memory Set (sentKeys)
    ├─ Fast lookup: O(1) per notification
    ├─ Prevents duplicates in current session
    └─► Check 2: Excel Sheet 7 Historical
        ├─ Database validation: All previous notifications
        ├─ Cross-session prevention
        └─► If BOTH say "NOT SENT":
            └─► CREATE & SEND notification
            └─► Add to sentKeys Set
            └─► Write to Excel Sheet 7
            └─► Record emailSent timestamp
```

**Example - Visa Expiry Warning:**
- Visa with expiry date 2026-05-15
- Current date: 2026-05-10 (5 days before)
- Notification key: "VISA_EXPIRY_WARNING_visa123"
- Check 1: Is key in `sentKeys` Set? (memory check)
- Check 2: Is key in Sheet 7? (Excel history check)
- Only if BOTH are false: Send email notification

---

## Data Management

### Excel File Structure (data.xlsx)

```
Sheet 1: Users
├─ Columns: UserId | Username | Password | Role | Email | CreatedDate
├─ Primary Key: UserId
└─ Purpose: Authentication & authorization

Sheet 2: Visitors
├─ Columns: VisitorId | Name | Nationality | Passport | DOB | Phone | Email | Address | RegisteredDate
├─ Primary Key: VisitorId
└─ Purpose: Visitor profile storage

Sheet 3: Visa Applications
├─ Columns: AppId | VisitorId | Purpose | Duration | SubmissionDate | Status | RejectionReason
├─ Foreign Key: VisitorId → Sheet 2
└─ Purpose: Track visa applications

Sheet 4: Visas
├─ Columns: VisaId | VisitorId | AppId | IssueDate | ExpiryDate | Status | IsActivated
├─ Foreign Keys: VisitorId → Sheet 2, AppId → Sheet 3
└─ Purpose: Approved visa records

Sheet 5: Border Entry Records
├─ Columns: EntryId | VisitorId | VisaId | EntryDate | ExitDate | EntryStatus | OverstayStatus
├─ Foreign Keys: VisitorId → Sheet 2, VisaId → Sheet 4
└─ Purpose: Entry/exit transaction log

Sheet 6: Warrants
├─ Columns: WarrantId | VisitorId | IssueDate | Reason | DaysOverstay | Status | ClosedDate
├─ Foreign Key: VisitorId → Sheet 2
└─ Purpose: Legal warrant records for overstays

Sheet 7: Notifications
├─ Columns: NotifId | VisitorId | Type | Message | CreatedDate | EmailSent | ReadStatus
├─ Foreign Key: VisitorId → Sheet 2
└─ Purpose: Notification history & audit log
```

### Data Relationships

```
User
 └─ created/managed records

Visitor (Hub)
 ├─► VisaApplication(s)
 │    └─► Visa (approved application)
 │         └─► BorderEntryRecord(s)
 │              └─► Warrant (if overstay > 2 days)
 │
 ├─► BorderEntryRecord(s)
 │
 ├─► Warrant(s)
 │
 └─► Notification(s)
```

### CRUD Operations

#### Create (Write)
```java
// Visitor
BSFFunctions.writeVisitorToExcel(visitor);

// Visa Application
BSFFunctions.writeVisaApplicationToExcel(application);

// Approved Visa
BSFFunctions.writeVisaToExcel(visa);

// Entry Record
BSFFunctions.writeBorderEntryRecordToExcel(entryRecord);

// Warrant
BSFFunctions.writeWarrantToExcel(warrant);

// Notification
BSFFunctions.writeNotificationToExcel(notification);
```

#### Read (Load)
```java
// Load all data at startup
BSFFunctions.loadUsersFromExcel(users);
BSFFunctions.loadVisitorsFromExcel(visitors, users);
BSFFunctions.loadVisaApplicationsFromExcel(applications, visitors);
BSFFunctions.loadVisasFromExcel(visas, visitors, applications);
BSFFunctions.loadBorderEntryRecordsFromExcel(entryRecords, visitors, visas);
BSFFunctions.loadWarrantsFromExcel(warrants, visitors);
BSFFunctions.loadNotificationsFromExcel(notifications, visitors);
```

#### Update (Modify)
```java
// Update existing records
BSFFunctions.updateVisitorInExcel(visitor, rowNumber);
BSFFunctions.updateVisaApplicationInExcel(application, rowNumber);
BSFFunctions.updateVisaInExcel(visa, rowNumber);
BSFFunctions.updateBorderEntryRecordInExcel(entryRecord, rowNumber);
BSFFunctions.updateWarrantInExcel(warrant, rowNumber);
```

#### Display (View)
```java
// Formatted output to console
DisplayExcelData.displayAllVisitors(visitors);
DisplayExcelData.displayAllVisaApplications(applications);
DisplayExcelData.displayAllVisas(visas);
DisplayExcelData.displayAllBorderEntryRecords(entryRecords);
DisplayExcelData.displayAllWarrants(warrants);
DisplayExcelData.displayAllNotifications(notifications);
```

---

## Best Practices

### 1. **Separation of Concerns**
Each class has a single responsibility:

| Class | Responsibility |
|-------|-----------------|
| `NotificationManager` | Notification creation & dispatch logic |
| `NotificationSender` | Email delivery only |
| `NotificationService` | Background monitoring only |
| `WriteDataToExcel` | Data persistence to Excel |
| `DisplayExcelData` | Data formatting & display |
| `BSFFunctions` | Unified facade/delegator |

### 2. **DRY Principle (Don't Repeat Yourself)**
- 15 dedicated notification methods prevent code duplication
- All notifications follow identical creation and sending flow
- Message generation centralized in NotificationManager

### 3. **Type Safety**
- Notification types use Java Enum (15 values)
- Compile-time validation prevents invalid notification types
- Self-documenting code through enum names

### 4. **Thread Safety**
```java
// Synchronized list protection
synchronized (notifications) {
    notifications.add(notification);
}

// Volatile flag for clean shutdown
volatile boolean stopService = false;

// Per-row error handling prevents cascade failures
try {
    // Process row
} catch (Exception e) {
    // Log but continue to next row
}
```

### 5. **Error Resilience**
- Per-row exception handling in background threads
- Email failure doesn't break Excel persistence
- Excel persistence failure logs but continues monitoring
- All operations logged with timestamps

### 6. **Maintainability**
- Clear method naming: `notifyVisaExpiryWarning()`, `notifyWarrantCreated()`
- Consistent notification flow across all 15 types
- Easy to add new notification types (just add enum value + method)

### 7. **Testability**
- Static methods allow unit testing without mocking
- Each NotificationManager method independently testable
- Clear input/output contracts
- Test classes included: `TestMail.java`, `TestPOI.java`

### 8. **Performance**
- Daemon threads use efficient polling intervals (60s, 15s)
- In-memory Set for O(1) duplicate checking
- Excel operations batched where possible
- SMTP timeout settings prevent hanging

---

## Configuration

### Email Configuration (AppConfig.java)

Edit `src/AppConfig.java` to configure email notifications:

```java
public class AppConfig {
    // Gmail SMTP Configuration
    public static final String ADMIN_EMAIL = "your-email@gmail.com";
    public static final String ADMIN_PASSWORD = "your-app-password";  // NOT regular password
    public static final String SMTP_HOST = "smtp.gmail.com";
    public static final int SMTP_PORT = 587;
    public static final boolean USE_TLS = true;
    
    // Email Settings
    public static final String SENDER_NAME = "BSF Entry Management";
    public static final String NOTIFICATION_SUBJECT_PREFIX = "[BSF ALERT]";
    
    // Monitoring Configuration
    public static final long VISA_CHECK_INTERVAL = 60000;     // 60 seconds
    public static final long OVERSTAY_CHECK_INTERVAL = 15000; // 15 seconds
    public static final int VISA_EXPIRY_WARNING_DAYS = 5;     // 5 days before expiry
    public static final int WARRANT_THRESHOLD_DAYS = 2;       // Warrant after 2 days overstay
}
```

### Gmail Setup (Recommended)

1. **Enable 2-Factor Authentication**
   - Google Account Settings → Security

2. **Generate App Password**
   - Create 16-character app password (not account password)
   - Use this as `ADMIN_PASSWORD` in AppConfig.java

3. **Allow Less Secure Apps** (Alternative)
   - If app password not working, enable in account settings

### Testing Email Configuration

Use `TestMail.java` to verify setup:

```bash
java -cp "bin:lib/*" TestMail
```

Expected output:
```
Testing SMTP Configuration...
Connecting to smtp.gmail.com:587...
✓ Connection successful
✓ Authentication successful
✓ Test email sent to: admin@gmail.com
```

---

## FAQ

### Q1: **What happens if the email server is down?**
**A:** Notifications are created and persisted to Excel regardless. When email server is back up, notifications marked with `emailSent=false` can be resent manually (Option 8 in admin menu).

### Q2: **Can I add more notification types?**
**A:** Yes! Easy 3-step process:
1. Add new enum value to `NotificationType` in `Notification.java`
2. Create new method in `NotificationManager.java`
3. Call from relevant location in your workflow

### Q3: **How are visa expiry dates calculated?**
**A:** `expiryDate = issueDate + intendedDuration (days)`
- Example: Issued 2026-05-10, 30-day visa → Expires 2026-06-09

### Q4: **What defines an "overstay"?**
**A:** Any visitor who remains past their visa expiry date:
- Overstay starts: Day after visa expiry
- Warning: Sent immediately upon detection
- Warrant: Created if overstay > 2 days

### Q5: **How often are visa expirations checked?**
**A:** Every 60 seconds by background daemon thread
- Runs continuously while system is active
- Checks ALL visas in the system
- 5-day warning window before actual expiry

### Q6: **How often are overstays detected?**
**A:** Every 15 seconds by background daemon thread
- Checks ALL border entry records
- Calculates current overstay duration
- Auto-generates warrants for > 2-day overstays

### Q7: **Can admins delete records?**
**A:** Yes, via Option 7 (Delete Records):
- Delete visitor (cascade deletes related records)
- Delete visa application
- Delete entry record
- Confirmation prompt prevents accidental deletion

### Q8: **Is there an audit trail of all notifications?**
**A:** Yes! Excel Sheet 7 contains complete notification history:
- All 15 notification types logged
- Timestamp of creation and email send
- Recipient and message content
- Read status tracking

### Q9: **What if two admin users access the system simultaneously?**
**A:** Excel file is shared:
- Last-write-wins for updates
- No built-in file locking
- Recommend: Single admin at a time OR implement file versioning

### Q10: **Can visitors register themselves, or only admins?**
**A:** Currently admin-only (Option 1 in menu):
- Users can login but see admin menu only
- Future enhancement: Self-registration portal for visitors

---

## Troubleshooting

### ❌ Issue: "File not found: data.xlsx"

**Solution:** Create the file or copy from git repository
```bash
# If corrupted or missing
cp data.xlsx.backup data.xlsx
```

### ❌ Issue: "SMTP Connection Failed"

**Solution:** 
1. Verify AppConfig.java credentials
2. Check email address is Gmail (other providers use different settings)
3. For Gmail, use App Password (not account password)
4. Ensure "Allow less secure apps" is enabled
5. Run TestMail.java to diagnose

### ❌ Issue: "Java compilation errors"

**Solution:**
1. Verify library files are in `lib/` directory
2. Check VS Code settings: `.vscode/settings.json`
3. Verify `sourceCompatibility` is 8 or higher
4. Clean rebuild:
   ```bash
   rm -rf bin/*
   javac -cp "lib/*" -d bin src/*.java
   ```

### ❌ Issue: "Notifications not being sent automatically"

**Solution:**
1. Check NotificationService.java started:
   ```bash
   jps -l | grep NotificationService
   ```
2. Verify AppConfig.java email settings
3. Check Excel Sheet 7 for notification records (proves system is creating them)
4. Check console for error messages
5. Restart application:
   ```bash
   java -cp "bin:lib/*" BSFEntryManagementSystem
   ```

### ❌ Issue: "Excel file locked by another process"

**Solution:**
1. Close Excel application if open
2. Check for `~$data.xlsx` file (Excel temporary lock file)
3. Wait 30 seconds for file lock to release
4. Restart application
5. If persistent, check if another Java process is accessing file

### ❌ Issue: "Cannot find main class"

**Solution:**
1. Verify bin directory exists and contains compiled `.class` files
2. Recompile:
   ```bash
   javac -cp "lib/*" -d bin src/*.java
   ```
3. Run from project root directory
4. Check CLASSPATH includes all jar files

### ❌ Issue: "Duplicate notifications being sent"

**Solution:**
1. This should NOT happen (duplicate prevention is built-in)
2. If occurring, check:
   - Excel Sheet 7 for previous notification records
   - In-memory Set implementation
   - System timestamp accuracy
3. Contact development team with:
   - Screenshot of Excel Sheet 7
   - Console logs
   - Notification details

### ❌ Issue: "Warrant not created for overstay"

**Solution:**
1. Check overstay duration > 2 days
2. Verify NotificationService.java background thread is running
3. Check overstayLoop() is checking every 15 seconds
4. View all data (Option 5) to verify entry record exists
5. Check Excel Sheet 6 for warrant records

---

## Performance Characteristics

| Operation | Time Complexity | Notes |
|-----------|-----------------|-------|
| Load all data | O(n) | n = total records, read at startup |
| Register visitor | O(1) | Write to Excel sheet |
| Submit visa application | O(1) | Write to Excel sheet |
| Check visa expiry (daemon) | O(n) | n = total visas, runs every 60s |
| Check overstays (daemon) | O(m) | m = total entry records, runs every 15s |
| Duplicate prevention | O(1) | In-memory Set lookup |
| Send email | O(1) | SMTP network operation |
| Persist notification | O(1) | Write to Excel sheet |

### Scalability Limits
- **Visitors**: ~5,000-10,000 (depends on CPU/RAM)
- **Daily notifications**: ~1,000-5,000 (email rate limiting may apply)
- **Concurrent users**: ~5-10 (single Excel file bottleneck)
- **Background threads**: 2 daemon threads (fixed overhead)

---

## System Requirements

### Minimum
- **OS**: Windows 7+, Linux, macOS
- **Java**: JDK 8 or higher
- **RAM**: 512 MB
- **Storage**: 100 MB
- **Network**: For email notifications

### Recommended
- **OS**: Windows 10/11, Ubuntu 20.04+, macOS 10.15+
- **Java**: JDK 11 or higher
- **RAM**: 2 GB
- **Storage**: 1 GB SSD
- **Network**: Stable internet connection for SMTP

### External Dependencies
- **Apache POI**: Excel file manipulation (included in lib/)
- **Java Mail API**: SMTP email delivery (included in lib/)
- **Gmail/SMTP Server**: For email notifications

---

## Future Enhancements

Potential features for version 2.0:

- [ ] **Web Interface**: Replace console with web dashboard
- [ ] **Database Backend**: Replace Excel with PostgreSQL/MySQL
- [ ] **Multi-user Concurrency**: Proper database locking
- [ ] **Mobile App**: Border officer mobile check-in
- [ ] **Analytics Dashboard**: Visa/entry statistics and charts
- [ ] **Advanced Search**: Query builder for records
- [ ] **Export Functionality**: PDF/CSV reports
- [ ] **Biometric Integration**: Photo/fingerprint storage
- [ ] **SMS Notifications**: SMS alerts in addition to email
- [ ] **API Integration**: Third-party system connectivity
- [ ] **Audit Logging**: Detailed activity logs
- [ ] **Two-Factor Authentication**: Enhanced security

---

## Contributing

### Coding Standards
- Follow Google Java Style Guide
- 4-space indentation
- `camelCase` for methods/variables
- `PascalCase` for classes
- Max 100 characters per line
- Comment complex logic

### Testing
Before committing:
1. Compile without errors: `javac -cp "lib/*" -d bin src/*.java`
2. Run TestMail.java for email functionality
3. Run TestPOI.java for Excel functionality
4. Test all menu options manually

### Commit Messages
```
[FEATURE] Add visa status update
[BUGFIX] Fix duplicate notification sending
[REFACTOR] Extract email formatting method
[DOCS] Update README with new procedures
```

---

## License

This project is developed for Border Security Force (BSF) use.
All rights reserved. Unauthorized distribution prohibited.

---

## Support & Contact

### Documentation Files
- **ARCHITECTURE.md** - Detailed system architecture & diagrams
- **BEST_PRACTICES.md** - Implementation patterns & principles
- **IMPLEMENTATION_SUMMARY.md** - Code refactoring details
- **INTEGRATION_GUIDE.md** - Usage examples & integration points

### For Issues & Questions
1. Check FAQ section above
2. Check Troubleshooting section
3. Review relevant documentation file
4. Contact development team with detailed error logs

---

## Version History

| Version | Date | Changes |
|---------|------|---------|
| 1.0 | Dec 2025 | Initial release with 15 notification types |
| 0.9 | Nov 2025 | Background monitoring (visa expiry & overstay) |
| 0.8 | Oct 2025 | Notification system implementation |
| 0.7 | Sep 2025 | Core entity models |
| 0.6 | Aug 2025 | Excel I/O operations |

---

## Last Updated
**December 10, 2025** - Production Ready ✅

---

**Happy Border Management! 🛂**

*For issues or feature requests, contact the development team.*
