# UC-013: Admin Manage Contact Enquiries

**Use Case ID:** UC-013  
**Name:** Admin Manage Contact Enquiries  
**Version:** 1.0  
**Related Flows:** FL-021  
**Related Domain Concepts:** DC-009 (ContactUs)

---

## Description
An administrator views all contact enquiries submitted by guests and customers and may delete any entry.

## Actors
| Actor | Role |
|---|---|
| **Admin** | Primary actor — reviews and removes contact enquiries |
| **System** | Retrieves enquiry data and performs deletion |

## Preconditions
- The admin is authenticated.
- The admin is on the "Contact Us" admin table page (`table_contactus.jsp`).

## Postconditions
- If deletion is triggered: the selected enquiry is removed from the database.
- The contact enquiry list refreshes.

## Business Requirements

| BUREQ ID | Requirement |
|---|---|
| BUREQ-013-01 | The admin must be able to view all submitted contact enquiries with name, email, phone, and message. |
| BUREQ-013-02 | The admin must be able to delete any contact enquiry. |
| BUREQ-013-03 | After deletion, the enquiry list must reflect the removal. |

## Main Flow

| Step | Actor | Action |
|---|---|---|
| 1 | Admin | Navigates to the contact enquiries table page. |
| 2 | System | Retrieves all contact enquiry records. |
| 3 | System | Displays the enquiry list. |
| 4 | Admin | Clicks "Remove" next to an enquiry. |
| 5 | System | Deletes the enquiry record by ID. |
| 6 | System | Refreshes the enquiry list. |

## Alternative Flows

### AF-013-A: No Enquiries
- At Step 2, if no enquiries exist, the page displays an empty state.
