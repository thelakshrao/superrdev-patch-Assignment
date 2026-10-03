# NOTES

## Summary of changes
1. **SQL:** added brackets around the OR so the status and archived filters apply to every row. Done total changed from 49 to 5.
2. **Backend:** removed an artificial `Thread.sleep` that delayed short searches by up to 1 second (1.025 s to 0.008 s).
3. **React:** debounced the search and cancelled old requests. Typing "api" now sends 1 request instead of 3.
4. **React:** fixed "Loading tasks..." showing forever when the backend fails. The error now shows and clears on recovery.
5. **React:** reset to page 1 when search or status changes, so the user no longer lands on an empty page.
6. **Backend:** bad `status`, `page` or `pageSize` now returns 400 instead of 500. `pageSize` is capped at 100.
7. **Backend:** paging now happens in the database (LIMIT/OFFSET plus COUNT), not in Java memory. API output is identical (checked with diff).

My handwritten explanations are in the `handwritten/` folder.

## What I chose not to change
- The `LIKE '%term%'` search still scans the table. An index would not help it.
- I added no automated tests. I tested each fix by hand with curl and the browser Network tab.
- I reject bad input instead of silently correcting it. This is a choice.

## Biggest remaining risk
No automated tests, so a small change to the query or controller could bring a bug back unnoticed. The same SQL is also copied in the H2 and Oracle files and must be kept in sync by hand.

## Tools and AI used
I used Claude.ai to understand the bugs and draft fixes. I ran every test and applied every change myself, and wrote the handwritten notes in my own words.
