package ru.sber.streams

import java.util.stream.Collector

// 1. Используя withIndex() посчитать сумму элементов листа, индекс которых кратен 3. (нулевой индекс тоже входит)
fun getSumWithIndexDivisibleByThree(list: List<Long>): Long =
    list.withIndex().filter { it.index % 3 == 0 }.sumOf { it.value }

// 2. Используя функцию generateSequence() создать последовательность, возвращающую числа Фибоначчи.
fun generateFibonacciSequence(): Sequence<Int> =
    generateSequence(Pair(0, 1)) { Pair(it.second, it.first + it.second) }.map { it.first }


// 3. Получить города, в которых есть покупатели.
fun Shop.getCustomersCities(): Set<City> = customers.map { it.city }.toSet()

// 4. Получить все когда-либо заказанные продукты.
fun Shop.allOrderedProducts(): Set<Product> =
    customers.flatMap { it.orders }.flatMap { it.products }.toSet()

// 5. Получить покупателя, который сделал больше всего заказов.
fun Shop.getCustomerWithMaximumNumberOfOrders(): Customer? =
    customers.maxWithOrNull(compareBy { it.orders.size })

// 6. Получить самый дорогой продукт, когда-либо приобртенный покупателем.
fun Customer.getMostExpensiveProduct(): Product? =
    orders.flatMap { it.products }.maxWithOrNull(compareBy { it.price })

// 7. Получить соответствие в мапе: город - количество заказанных и доставленных продуктов в данный город.
fun Shop.getNumberOfDeliveredProductByCity(): Map<City, Int> =
    customers.groupingBy { it.city }.aggregate { _, accumulator: Int?, element, first ->
        if (first)
            element.orders.filter { it.isDelivered }.flatMap { it.products }.count()
        else
            accumulator!! + element.orders.filter { it.isDelivered }.flatMap { it.products }.count()
    }

// 8. Получить соответствие в мапе: город - самый популярный продукт в городе.
fun Shop.getMostPopularProductInCity(): Map<City, Product> =
    customers.groupingBy { it.city }.aggregate { _, accumulator: Map<Product, Int>?, element, first ->
        if (first)
            element.orders.map { it.products }.flatten().groupingBy { it }.eachCount()
        else {
            accumulator!!.toMutableMap().apply {
                element.orders.map { it.products }.flatten().groupingBy { it }.eachCount().forEach { key, value ->
                    merge(key, value) { currentValue, addedValue -> currentValue + addedValue }
                }
            }
            accumulator.toMap()
        }
    }.map { entry ->
        Pair(entry.key, entry.value.maxByOrNull { x -> x.value }!!.key)
    }.toMap()


// 9. Получить набор товаров, которые заказывали все покупатели.
fun Shop.getProductsOrderedByAll(): Set<Product> =
    customers.flatMap { it.orders }.flatMap { it.products }.groupingBy { it }
        .aggregate { _, accumulator: Int?, _, first ->
            if (first)
                1
            else
                accumulator!! + 1
        }.filter { it.value == customers.size }.keys
