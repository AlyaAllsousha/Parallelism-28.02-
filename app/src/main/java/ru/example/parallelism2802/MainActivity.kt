package ru.example.parallelism2802

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.example.parallelism2802.ui.theme.Parallelism2802Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Parallelism2802Theme {

                val stateColor =  remember{ mutableStateOf(Color.Blue) }
                Thread(
                    Runnable {
                        while(true){
                            stateColor.value = Color.Red
                            Thread.sleep(1000)
                            stateColor.value = Color.Green
                            Thread.sleep(1000)
                            stateColor.value = Color.Cyan
                            Thread.sleep(1000)
                            stateColor.value = Color.Magenta
                            Thread.sleep(1000)
                        }
                    }
                ).start()
                runBlocking(Dispatchers.IO){
                    var sum1 = 0
                    var sum2 = 0
                    var sum3 = 0
                    var sum4 = 0

                    val time1 = System.currentTimeMillis()
                    val job1 = launch{
                        sum1 = sum(1, 10000)
                    }
                    val job2 = launch{
                        sum2 = sum(10001, 20000)
                    }
                    val job3 =launch{
                        sum3 = sum(20001, 30000)
                    }
                   joinAll(job1, job2, job3)
                   val time2 = System.currentTimeMillis()
                   val job4 = launch{
                       sum4 = sum(1, 30000)
                   }
                    joinAll(job4)

                   val time3 = System.currentTimeMillis()
                    Log.d("Otvet", "${sum1 + sum2 + sum3} ${time2-time1}")
                    Log.d("Otvet2", "${sum4} ${time3-time2}")
                }
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android", stateColor)
                }
            }
        }
    }
}
suspend fun sum(ot:Int, doo:Int ):Int{
    var sum = 0
    for(i in ot..doo){
        sum += i
    }
    return sum
}
@Composable
fun Greeting(name: String, stateColor: MutableState<Color>, )  {
    Text(
        color = stateColor.value,
        text = "Hello $name!",
        modifier = Modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Parallelism2802Theme {
        Greeting("Android", mutableStateOf(Color.Blue))
    }
}