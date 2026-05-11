package Zomato.Copy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.launch

// ViewModel to handle API logic
class FoodViewModel : ViewModel() {
    var restaurantApiList by mutableStateOf<List<Restaurant>>(emptyList())
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun fetchRestaurants() {
        viewModelScope.launch {
            isLoading = true
            try {
                // Calling the Spring Boot API via Retrofit
                val response = apiService.getRestaurants()
                restaurantApiList = response
                errorMessage = null
            } catch (e: Exception) {
                errorMessage = "Failed to load: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController, viewModel: FoodViewModel = viewModel()) {
    val greenPrimary = Color(0xFF2E7D32)

    // Fetch data when screen opens
    LaunchedEffect(Unit) {
        viewModel.fetchRestaurants()
    }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = greenPrimary, selectedTextColor = greenPrimary)
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { },
                    icon = { Icon(Icons.Default.ShoppingBag, contentDescription = "Orders") },
                    label = { Text("Orders") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { },
                    icon = { Icon(Icons.Default.Favorite, contentDescription = "Favorite") },
                    label = { Text("Favorite") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile") }
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF8F8F8))
        ) {
            // Header Section
            item { HeaderSection(greenPrimary) }

            // Search Bar
            item { SearchBarSection() }

            // Categories Section
            item { CategoriesSection(greenPrimary) }

            // Promotion Banner
            item { PromotionBanner(greenPrimary) }

            // Popular Restaurants Title
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Popular Restaurants",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = "See all",
                        fontSize = 14.sp,
                        color = greenPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Loading / Error Handling
            if (viewModel.isLoading) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(50.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = greenPrimary)
                    }
                }
            } else if (viewModel.errorMessage != null) {
                item {
                    Text(
                        text = viewModel.errorMessage!!,
                        color = Color.Red,
                        modifier = Modifier.padding(20.dp)
                    )
                }
            }

            // Restaurant List from API
            items(viewModel.restaurantApiList) { restaurant ->
                RestaurantCard(restaurant, greenPrimary)
            }

            // Show a fallback if list is empty
            if (!viewModel.isLoading && viewModel.restaurantApiList.isEmpty()) {
                item {
                    Text("No restaurants found", modifier = Modifier.padding(20.dp))
                }
            }
        }
    }
}

@Composable
fun HeaderSection(greenPrimary: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = greenPrimary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Deliver to",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            Text(
                text = "HSR Layout, Bangalore",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Notifications,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarSection() {
    OutlinedTextField(
        value = "",
        onValueChange = {},
        placeholder = { Text("Search for food, restaurants...", color = Color.Gray, fontSize = 14.sp) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
        trailingIcon = { Icon(Icons.Default.FilterList, contentDescription = null, tint = Color.Gray) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(56.dp),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
        )
    )
}

@Composable
fun CategoriesSection(greenPrimary: Color) {
    val categories = listOf(
        CategoryItem("Pizza", Icons.Default.LocalPizza),
        CategoryItem("Burger", Icons.Default.Fastfood),
        CategoryItem("Ice Cream", Icons.Default.Icecream),
        CategoryItem("Drinks", Icons.Default.LocalDrink),
        CategoryItem("Coffee", Icons.Default.Coffee)
    )

    Column(modifier = Modifier.padding(vertical = 15.dp)) {
        Text(
            text = "Categories",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            items(categories) { category ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(65.dp)
                            .clip(RoundedCornerShape(15.dp))
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            category.icon,
                            contentDescription = null,
                            tint = greenPrimary,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = category.name, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

@Composable
fun PromotionBanner(greenPrimary: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .height(150.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = greenPrimary)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(20.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Get 50% OFF", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                Text(text = "On your first order", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 5.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Text(text = "Order Now", color = greenPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
            Box(
                modifier = Modifier
                    .weight(0.8f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Fastfood, contentDescription = null, tint = Color.White.copy(alpha = 0.3f), modifier = Modifier.size(100.dp))
            }
        }
    }
}

@Composable
fun RestaurantCard(restaurant: Restaurant, greenPrimary: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(Color.LightGray)
            ) {
                Icon(Icons.Default.Restaurant, contentDescription = null, modifier = Modifier.align(Alignment.Center).size(50.dp), tint = Color.Gray)
                
                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White.copy(alpha = 0.9f))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFB300), modifier = Modifier.size(16.dp))
                    Text(text = (restaurant.rating ?: 0.0).toString(), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
            Column(modifier = Modifier.padding(15.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = restaurant.name ?: "Unknown", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(text = restaurant.price ?: "", fontSize = 14.sp, color = greenPrimary, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(5.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Timer, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                    Text(text = " ${restaurant.time ?: 0} mins", fontSize = 12.sp, color = Color.Gray)
                    Text(text = " • ", color = Color.Gray)
                    Text(text = restaurant.type ?: "General", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }
    }
}

data class CategoryItem(val name: String, val icon: ImageVector)
