# Selenium + Java — Automated E-Commerce Mini-Project

This document presents a full academic and technical overview of an automated test suite built for the Tealium Demo E-Commerce application using **Selenium WebDriver**, **Java**, **TestNG**, and the **Page Object Model (POM)** design pattern.  
The purpose is to demonstrate structured UI automation, verification of functional requirements, reusable architectural design, and correct scenario coverage required by course specifications.

---

# 1. Abstract

This mini-project implements an end-to-end automation suite covering account creation, login, navigation, product filtering, sorting, wishlist operations, cart validation, and UI styling checks.  
It demonstrates core automation skills: element interaction, explicit waits, assertions, state management, and multi-step scenario flow using TestNG.

---

# 2. Project Requirements Mapping

Below is a detailed academic mapping of each requirement to the exact implementation approach and framework features used.

| Requirement | Implemented Using | Details |
|------------|------------------|---------|
| **1. Automate scenarios using Selenium + Java** | Selenium WebDriver API | `click()`, `sendKeys()`, `Actions`, `WebDriverWait`, navigation |
| **2. Use JUnit / TestNG** | TestNG | `@Test`, `@BeforeMethod`, `@AfterMethod`, `testng.xml` for suite orchestration |
| **3. Page Object Model (POM)** | `core.pages`, `core.elements` | Each page has dedicated locators + actions (clean separation) |
| **4. Use asserts for verification** | TestNG `Assert` class | Validating titles, messages, counts, styling, totals |
| **5. Use wait methods (avoid Thread.sleep)** | `WebDriverWait`, `ExpectedConditions`, timeout from config | Applied in each page method requiring synchronization |
| **6. Screenshot on failure** | Custom TestNG Listener | Captures browser screenshot on test failure and stores in `/screenshots` |
| **7. Configure report (optional)** | TestNG default HTML report | Automatically generated under `/target/surefire-reports` |
| **8. Add any feature** | Dynamic email generator + shared runtime state + menu hover automation | Enhances realism of test scenarios |
| **9. Upload to Git** | Git & GitHub | Repository structured for academic submission & portfolio use |

---

# 3. Application Under Test (AUT)

**URL:** https://ecommerce.tealiumdemo.com/

Features used for testing:

- Account registration
- Login session management
- Women / Men / Sale categories
- Product cards & hover overlays
- Color & price filters
- Sorting by price
- Wishlist handling
- Shopping cart
- Success/empty-state messages

---

# 4. Test Architecture (POM)

The automation suite follows the Page Object Model for maintainability.

```text
src/test/java/core
│
├── tests/        → Test Classes (Test1–Test8)
├── pages/        → Page Objects (HomePage, LoginPage…)
├── elements/     → Locators (By objects)
├── utilities/    → DriverFactory, WaitUtils, ConfigurationReader, BaseTest
├── listeners/    → Screenshot Listener (TestListener)
└── globals/      → Shared test data (Globals: email, password)
```

**Design benefits:**

- Reusability
- Readability
- Maintainability
- Reduced duplication

---

# 5. UML Architecture & Flow (High-Level)

This section provides a simplified UML-style view of the architecture and execution flow.

## 5.1 Structural View (Class-Level)

```text
+--------------------+        uses         +----------------------+
|    Test Classes    |  ---------------->  |     Page Objects     |
| core.tests.*       |                    | core.pages.*         |
+--------------------+                    +----------------------+
           |                                         |
           | depend on                               | use
           v                                         v
+--------------------+                    +----------------------+
|   BaseTest         |                    |  Element Classes     |
| (setup/teardown)   |                    | core.elements.*      |
+--------------------+                    +----------------------+
           |                                         ^
           | uses                                    |
           v                                         |
+--------------------+                    +----------------------+
|  DriverFactory     |                    |  Globals / Utilities |
|  (WebDriver)       |                    | core.globals.*       |
+--------------------+                    | core.utilities.*     |
                                           +----------------------+
```

Core ideas:

- **Tests** call **Page Objects**
- **Page Objects** use **Element locators**
- **BaseTest** manages WebDriver lifecycle
- **Utilities** (waits, config) support all layers
- **Globals** share dynamic credentials between tests

## 5.2 Execution Flow View (Scenario-Level)

```text
Test1_CreateAccount
    ↓ creates
User (email/password) stored in Globals
    ↓
Test2_SignIn
    ↓
Test3_HoverStyle
    ↓
Test4_SaleProductsStyle
    ↓
Test5_PageFilters
    ↓
Test6_Sorting (adds items to wishlist)
    ↓
Test7_ShoppingCartTest (moves items to cart & validates totals)
    ↓
Test8_EmptyShoppingCartTest (clears cart & validates empty state)
```

This visualizes the **end-to-end customer journey** automated by the test suite.

---

# 6. Execution Model (Suite Dependency & testng.xml)

The test suite uses a **stateful, sequential execution model**. Several tests depend on data and state produced by earlier tests, e.g.:

- **Test1** dynamically creates a new account and stores credentials in `Globals`
- **Test2–6** reuse those credentials for sign-in and browsing flows
- **Test6** populates the wishlist
- **Test7** moves wishlist items into the shopping cart and validates totals
- **Test8** empties the cart, relying on items added in Test7

Because of these dependencies, running tests **individually** can break preconditions (e.g., empty wishlist/cart, missing user).

To guarantee correct ordering and state reuse, execution is orchestrated through:

```xml
<!-- testng.xml (simplified) -->
<suite name="Selenium MiniProject Suite">
    <test name="End-to-End E-Commerce Flow">
        <classes>
            <class name="core.tests.Test1_CreateAccount"/>
            <class name="core.tests.Test2_SignIn"/>
            <class name="core.tests.Test3_HoverStyle"/>
            <class name="core.tests.Test4_SaleProductsStyle"/>
            <class name="core.tests.Test5_PageFilters"/>
            <class name="core.tests.Test6_Sorting"/>
            <class name="core.tests.Test7_ShoppingCartTest"/>
            <class name="core.tests.Test8_EmptyShoppingCartTest"/>
        </classes>
    </test>
</suite>
```

**Why `testng.xml` is mandatory in this project:**

- Ensures deterministic test order
- Preserves shared state across tests
- Prevents precondition-related failures
- Reflects a realistic end-to-end user session

---

# 7. Configuration

`src/test/resources/config.properties`:

```properties
url=https://ecommerce.tealiumdemo.com/
timeoutSeconds=20
```

This keeps environment-specific data outside the code, improving flexibility and maintainability.

---

# 8. Tools & Technologies

| Category | Used |
|---------|------|
| Language | Java |
| Automation | Selenium WebDriver |
| Testing | TestNG |
| Pattern | Page Object Model |
| Build | Maven |
| Browser | Chrome |
| Reporting | TestNG HTML report |
| Failure Capture | Custom Screenshot Listener |

---

# 9. Detailed Scenario Documentation (Test Case Tables)

Below are the academic test case tables for each scenario specified in the coursework.

---

## **Test 1 — Create an Account**

| Step | Action | Expected Result |
|------|--------|-----------------|
| 1 | Navigate to homepage | Homepage loads |
| 2 | Open Account → Register | Registration page displayed |
| 3 | Verify page title | Title matches expected |
| 4 | Fill form with dynamic email | Valid input accepted |
| 5 | Click Register | Account is successfully created |
| 6 | Verify success message | Confirmation message visible |
| 7 | Log out | User returned to guest state |

---

## **Test 2 — Sign In**

| Step | Action | Expected |
|------|--------|----------|
| 1 | Navigate to homepage | Page loads |
| 2 | Go to Sign In | Login screen visible |
| 3 | Enter credentials from Test1 | Login accepted |
| 4 | Verify username on header | Welcome message visible |
| 5 | Log out | Session ends |

---

## **Test 3 — Hover Styling**

_Precondition: Signed In_

| Step | Action | Expected |
|------|--------|----------|
| 1 | View All Women | Product grid loads |
| 2 | Hover product | Overlay actions appear |
| 3 | Assert CSS changes | Hover style applied |

---

## **Test 4 — Sale Product Styling**

_Precondition: Signed In_

| Step | Action | Expected |
|------|--------|----------|
| 1 | View All Sale | Sale items load |
| 2 | Check dual price | Original + discounted visible |
| 3 | Validate original price style | Grey + strikethrough |
| 4 | Validate discounted price style | Blue + no strikethrough |

---

## **Test 5 — Page Filters**

_Precondition: Signed In_

| Step | Action | Expected |
|------|--------|----------|
| 1 | View All Men | Men page loads |
| 2 | Apply Black filter | All products have selected color border |
| 3 | Select price $0–$99.99 | Only 3 products remain |
| 4 | Validate product prices | All prices within range |

---

## **Test 6 — Sorting + Wishlist**

_Precondition: Signed In_

| Step | Action | Expected |
|------|--------|----------|
| 1 | View All Women | Page loads |
| 2 | Sort by Price | Products sorted ascending |
| 3 | Add first 2 to wishlist | Both added |
| 4 | Check wishlist count | “My Wishlist (2 items)” |

---

## **Test 7 — Shopping Cart Totals**

_Precondition: Test 6_

| Step | Action | Expected |
|------|--------|----------|
| 1 | Go to Wishlist | Items visible |
| 2 | Add both to cart | Items move to cart |
| 3 | Change quantity to 2 | Subtotal updates |
| 4 | Validate totals | Sum(subtotals) = Grand Total |

---

## **Test 8 — Empty Shopping Cart**

_Precondition: Test 7_

| Step | Action | Expected |
|------|--------|----------|
| 1 | Delete item 1 | Row count decreases |
| 2 | Repeat until all removed | Last item removed |
| 3 | Validate empty message | “You have no items…” |

---

# 10. Requirements → Test Traceability Matrix

This matrix links each high-level functional requirement from the assignment to the corresponding automated test and core assertion(s).

| Req ID | Requirement (from assignment) | Implemented In | Key Assertions / Validations |
|--------|-------------------------------|----------------|------------------------------|
| R1 | Create an Account | **Test1_CreateAccount** | Page title, success message, logout works |
| R2 | Sign In with created credentials | **Test2_SignIn** | Welcome banner contains username |
| R3 | Check hover style on products | **Test3_HoverStyle** | Actions visible only after hover |
| R4 | Check Sale products styling (old/new prices) | **Test4_SaleProductsStyle** | Old price = grey + strikethrough; New price = blue, no strikethrough |
| R5 | Check Men page filters (color + price) | **Test5_PageFilters** | Black selected with border; 3 products; all prices in range |
| R6 | Check Sorting + Wishlist (Women) | **Test6_Sorting** | Prices sorted asc (with allowed minor anomaly); wishlist shows “(2 items)” |
| R7 | Shopping Cart totals correct | **Test7_ShoppingCartTest** | Sum of row subtotals equals Grand Total |
| R8 | Empty Shopping Cart flow | **Test8_EmptyShoppingCartTest** | Row count decreases, final empty message appears |
| R9 | Use waits instead of Thread.sleep | All Page Objects | Widespread use of `WebDriverWait` & `ExpectedConditions` |
| R10 | Screenshot capture on failure | **TestListener + any failing test** | Screenshot file created under `/screenshots` |
| R11 | Use POM & TestNG suite | All tests via `testng.xml` | Execution controlled by suite, POM abstraction in pages/elements |

This table provides clear **academic traceability** between requirements and automated implementation, useful for both course evaluation and professional review.

---

# 11. Page Object API Documentation (Ultra-Short)

### HomePage
- `goToRegister()` — navigate to register page
- `goToSignIn()` — navigate to login page
- `logout()` — perform logout
- `getWelcomeMessage()` — read welcome banner
- `isUserLoggedIn()` — check login state
- `goToAllWomenPage()` — open Women category
- `goToAllMenPage()` — open Men category
- `goToAllSalePage()` — open Sale category
- `goToMyWishlist()` — open wishlist
- `getMyWishlistTextFromAccountMenu()` — read wishlist text/count

### LoginPage
- `login(email, password)` — performs login
- `getTitleText()` — returns page title text

### RegisterPage
- `fillRegisterForm(...)` — fills all fields
- `generateUniqueEmail()` — returns unique email per run
- `clickRegister()` — submits registration form
- `getSuccessMessage()` — reads success notification

### WomenPage
- `hoverLastProduct()` — performs hover on last product in grid
- `areLastProductActionsVisible()` — checks hover actions visibility
- `sortByPriceAscending()` — sorts products by price (ASC)
- `getAllProducts()` — returns list of product WebElements
- `getProductPrice(product)` — parses price from product card
- `arePricesSortedAscending(list)` — checks ascending order (with tolerance)
- `addProductToWishlistByIndex(i)` — adds product by index to wishlist

### MenPage
- `waitForPageToLoad()` — ensures `/men.html` is ready
- `getAllProducts()` — returns products in Men page
- `applyBlackColorFilter()` — clicks color Black filter
- `applyPriceFilter0To99()` — applies price interval 0–99.99
- `hasBlackColorSelectedWithBlueBorder(product)` — validates color swatch style
- `allProductsPriceBetween(list, min, max)` — validates price range

### SalePage
- `waitForPageToLoad()` — ensures Sale page is loaded
- `getSaleProducts()` — returns sale products
- `getOriginalPriceElement(product)` — old price locator wrapper
- `getFinalPriceElement(product)` — special price locator wrapper
- `getTextDecoration(element)` — returns text-decoration CSS value

### WishlistPage
- `waitForWishlistToLoad()` — ensures wishlist is ready
- `getWishlistItems()` — returns wishlist rows
- `addFirstNProductsToCart(n)` — moves first N wishlist items to cart
- `clickAddToCartForRow(row)` — clicks Add to Cart in a specific row

### ShoppingCartPage
- `waitForCartToLoad()` — ensures cart page is ready
- `setQuantityForFirstItemTo2AndUpdate()` — sets quantity to 2 and updates
- `getFirstRowSubtotal()` — returns subtotal of first row
- `getSumOfAllSubtotals()` — sums all row subtotals
- `getGrandTotal()` — reads grand total value
- `getCartItemCount()` — returns number of rows in cart
- `deleteFirstItemAndWait(previousCount)` — deletes first item and waits for row count to change
- `isCartEmptyMessageVisible()` — verifies empty-cart message presence

---

# 12. Results & Validation

The suite successfully validated:

- Functional correctness of key retail flows
- Visual styling consistency on hover and sale prices
- Correct behavior of Men page filters (color + price)
- Sorting logic on Women category (price ASC)
- Wishlist count updates after adding items
- Shopping cart total arithmetic (subtotals vs. grand total)
- Proper empty-state behavior when cart is cleared

The project demonstrates not only that tests pass, but also that they are designed and mapped **systematically** to the requirements.

---

# 13. Future Improvements

Potential enhancements:

- CI/CD integration (GitHub Actions, Jenkins, etc.)
- Advanced reporting (Allure, ExtentReports)
- Cross-browser and cross-platform testing
- Data-driven tests (CSV/Excel/JSON input)
- Parallel execution for performance
- Headless browser execution for CI pipelines

---

# 14. Installation & Execution

## 14.1 Install Dependencies

To install all Maven dependencies, open a terminal inside the project directory and run a full clean install.  
This ensures all required libraries (Selenium WebDriver, TestNG, WebDriverManager, etc.) are downloaded and compiled.

## 14.2 Run the Full Suite (Recommended)

### Running via IntelliJ IDEA

The suite can also be executed directly from the IDE:

1. In the Project panel, locate the `testng.xml` file.
2. Right-click on it.
3. Select **Run 'testng.xml'**.

This approach ensures that tests are executed sequentially and that shared runtime state (e.g., created user account, wishlist items, cart contents) flows correctly across the suite, as explained in Section 6.

---


# 15. Authors

- **Horgito Brahimaj**
- **Kleant Malasi**
- **Eleandro Pano**
- **Entjana Sala**
- **Mikaela Memushi**

---

# 16. License

This project is provided for academic purposes and as a demonstration of UI test automation and design principles.
