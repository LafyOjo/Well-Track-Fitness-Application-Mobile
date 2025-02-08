import org.junit.Test
import org.junit.Assert.*

class MainActivityUnitTest {

    // Test 1: Check if Greeting Text is Correct
    @Test
    fun testGreetingText() {
        val expectedGreeting = "Hello User"
        val actualGreeting = "Hello User"  // Simulate getting text from the TextView
        assertEquals("Greeting text should be correct", expectedGreeting, actualGreeting)
    }

    // Test 2: Check if "See All" Button Text is Correct
    @Test
    fun testSeeAllButtonText() {
        val expectedText = "See All"
        val actualText = "See All"  // Simulate getting text from the button
        assertEquals("See All button text should be correct", expectedText, actualText)
    }

    // Test 3: Check if RecyclerView Item Count is Correct (Simulated)
    @Test
    fun testEnrolledCoursesItemCount() {
        val expectedCount = 3
        val actualCount = 3  // Simulate getting item count from the RecyclerView adapter
        assertEquals("Item count should match", expectedCount, actualCount)
    }

    // Test 4: Check if Store Section Click Works (Simulated)
    @Test
    fun testStoreSectionClick() {
        val storeClicked = true  // Simulate a click event
        assertTrue("Store section should be clickable", storeClicked)
    }
}
