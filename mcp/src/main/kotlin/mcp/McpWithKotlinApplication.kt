package mcp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class McpWithKotlinApplication

fun main(args: Array<String>) {
	runApplication<McpWithKotlinApplication>(*args)
}
