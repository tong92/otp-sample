import scodec.bits.ByteVector
class TOTPTest extends munit.FunSuite:
  val seed = "3132333435363738393031323334353637383930"
  val seed32 = "3132333435363738393031323334353637383930313233343536373839303132"
  val seed64 = "31323334353637383930313233343536373839303132333435363738393031323334353637383930313233343536373839303132333435363738393031323334"
  val t0 = 0
  val x = 30
  val testTime = Array(59L, 1111111109L, 1111111111L, 1234567890L, 2000000000L, 20000000000L)
  val steps = "0"
  val crypts = Array("HmacSHA1", "HmacSHA256", "HmacSHA512")

  test("rfc-6238 8-length test"):
    val res = for
      time <- testTime
      (c, s) <- crypts.zip(Array(seed, seed32, seed64))
      gen = TOTP.gen(s, (time / x).toHexString, 8, c)
      _ = println(gen)
    yield gen
    val expect = List(
      "94287082", "46119246", "90693936",
      "07081804", "68084774", "25091201",
      "14050471", "67062674", "99943326",
      "89005924", "91819424", "93441116",
      "69279037", "90698825", "38618901",
      "65353130", "77737706", "47863826",
    )
    assertEquals(res.toList, expect)

  test("6-length test"):
    val res = for
      time <- testTime
      (c, s) <- crypts.zip(Array(seed, seed32, seed64))
      gen = TOTP.gen(s, (time / x).toHexString, 6, c)
      _ = println(gen)
    yield gen
    val expect = List(
      "287082", "119246", "693936",
      "081804", "084774", "091201",
      "050471", "062674", "943326",
      "005924", "819424", "441116",
      "279037", "698825", "618901",
      "353130", "737706", "863826",
    )
    assertEquals(res.toList, expect)
    