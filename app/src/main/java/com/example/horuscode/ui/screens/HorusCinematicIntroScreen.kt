package com.example.horuscode.ui.screens

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlin.math.*
import kotlin.random.Random

enum class IntroScene(val index: Int, val durationMs: Long) {
    SCENE_1_VOID(1, 3000L),
    SCENE_2_SYMBOLS(2, 3500L),
    SCENE_3_EYE_OF_HORUS(3, 3500L),
    SCENE_4_TITLE_REVEAL(4, 3500L),
    SCENE_5_PORTAL_OPEN(5, 2500L),
    SCENE_6_WELCOME(6, 0L)
}

data class StarlightParticle(
    val xRatio: Float,
    val yRatio: Float,
    val size: Float,
    val speed: Float,
    val baseAlpha: Float,
    val isGold: Boolean
)

data class FloatingGlyph(
    val glyph: String,
    val nameAr: String,
    val nameEn: String,
    val angle: Float,
    val distanceRatio: Float,
    val color: Color
)

@Composable
fun HorusCinematicIntroScreen(
    onFinish: () -> Unit,
    onSkip: () -> Unit,
    isArabic: Boolean,
    onToggleLanguage: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var currentScene by remember { mutableStateOf(IntroScene.SCENE_1_VOID) }
    var isHapticsEnabled by remember { mutableStateOf(true) }

    // Master timeline controller
    LaunchedEffect(currentScene) {
        if (currentScene != IntroScene.SCENE_6_WELCOME) {
            // Trigger gentle haptic on scene transitions
            if (isHapticsEnabled) {
                triggerHapticFeedback(context, currentScene)
            }
            delay(currentScene.durationMs)
            currentScene = when (currentScene) {
                IntroScene.SCENE_1_VOID -> IntroScene.SCENE_2_SYMBOLS
                IntroScene.SCENE_2_SYMBOLS -> IntroScene.SCENE_3_EYE_OF_HORUS
                IntroScene.SCENE_3_EYE_OF_HORUS -> IntroScene.SCENE_4_TITLE_REVEAL
                IntroScene.SCENE_4_TITLE_REVEAL -> IntroScene.SCENE_5_PORTAL_OPEN
                IntroScene.SCENE_5_PORTAL_OPEN -> IntroScene.SCENE_6_WELCOME
                IntroScene.SCENE_6_WELCOME -> IntroScene.SCENE_6_WELCOME
            }
        }
    }

    // Continuous ambient animations
    val infiniteTransition = rememberInfiniteTransition(label = "ambient_intro_transition")

    // Slow ambient rotation for celestial geometric rings
    val celestialRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(28000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "celestial_ring_rotation"
    )

    // Counter rotation for inner geometric ring
    val counterRotation by infiniteTransition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "counter_ring_rotation"
    )

    // Glowing energy pulsation
    val energyPulse by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "energy_pulse"
    )

    // Particle field state
    val particles = remember {
        val rand = Random(42)
        List(40) {
            StarlightParticle(
                xRatio = rand.nextFloat(),
                yRatio = rand.nextFloat(),
                size = rand.nextFloat() * 3f + 1.5f,
                speed = rand.nextFloat() * 0.05f + 0.02f,
                baseAlpha = rand.nextFloat() * 0.6f + 0.3f,
                isGold = rand.nextBoolean()
            )
        }
    }

    // Floating hieroglyph network definitions
    val glyphs = remember {
        listOf(
            FloatingGlyph("☥", "عنخ: الحياة", "Ankh: Life", 210f, 0.36f, HorusGold),
            FloatingGlyph("𓋹", "جد: الذاكرة", "Djed: Memory", 330f, 0.38f, HorusCyan),
            FloatingGlyph("𓆣", "جعران: التكرار", "Scarab: Loops", 90f, 0.37f, HorusAmberGold),
            FloatingGlyph("𓏛", "بردي: التدوين", "Papyrus: Output", 30f, 0.35f, HorusElectricCyan),
            FloatingGlyph("𓋖", "ماعت: ميزان المدخلات", "Ma'at: Input", 150f, 0.36f, Color(0xFFF8A5C2)),
            FloatingGlyph("𓊗", "هريم: الأوامر", "Pyramidion: Action", 270f, 0.38f, HorusLightGold)
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF0F1A3A),
                        Color(0xFF070E24),
                        Color(0xFF030612)
                    ),
                    center = Offset.Unspecified,
                    radius = 1400f
                )
            )
            .testTag("cinematic_intro_container")
    ) {
        // LAYER 1: Ambient Canvas (Starlight particles, temple gateway, celestial circuits)
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .testTag("intro_ambient_canvas")
        ) {
            val canvasW = size.width
            val canvasH = size.height
            val center = Offset(canvasW / 2f, canvasH / 2f)

            // Draw floating starlight particles
            particles.forEach { p ->
                val px = p.xRatio * canvasW
                val py = (p.yRatio * canvasH + (celestialRotation * p.speed * 8f)) % canvasH
                val pColor = if (p.isGold) HorusGold else HorusCyan
                drawCircle(
                    color = pColor.copy(alpha = (p.baseAlpha * energyPulse).coerceIn(0.1f, 0.9f)),
                    radius = p.size,
                    center = Offset(px, py)
                )
            }

            // SCENE 1: Draw Monumental Egyptian Doorway / Pylon Outline
            val doorwayAlpha = when (currentScene) {
                IntroScene.SCENE_1_VOID -> 0.75f * energyPulse
                IntroScene.SCENE_2_SYMBOLS -> 0.5f
                IntroScene.SCENE_3_EYE_OF_HORUS -> 0.4f
                IntroScene.SCENE_4_TITLE_REVEAL -> 0.3f
                IntroScene.SCENE_5_PORTAL_OPEN -> 0.85f * energyPulse
                IntroScene.SCENE_6_WELCOME -> 0.35f
            }

            drawTempleDoorway(center, canvasW, canvasH, doorwayAlpha)

            // SCENE 2, 3, 4, 5: Rotating Celestial Egyptian Astrolabe Rings
            if (currentScene.index >= 2) {
                val ringRadius = min(canvasW, canvasH) * 0.38f

                // Outer sacred ring
                drawCircle(
                    color = HorusGold.copy(alpha = 0.25f * energyPulse),
                    radius = ringRadius,
                    center = center,
                    style = Stroke(width = 1.5f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 25f), celestialRotation))
                )

                // Inner cyan circuit ring
                drawCircle(
                    color = HorusCyan.copy(alpha = 0.3f),
                    radius = ringRadius * 0.82f,
                    center = center,
                    style = Stroke(width = 1.2f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 16f), counterRotation))
                )

                // Sacred geometric 8-point star lines
                val starRadius = ringRadius * 0.65f
                for (i in 0 until 8) {
                    val angleRad = Math.toRadians((i * 45.0 + celestialRotation * 0.25))
                    val ptX = center.x + (starRadius * cos(angleRad)).toFloat()
                    val ptY = center.y + (starRadius * sin(angleRad)).toFloat()
                    drawLine(
                        color = HorusElectricCyan.copy(alpha = 0.18f),
                        start = center,
                        end = Offset(ptX, ptY),
                        strokeWidth = 1f
                    )
                }
            }

            // SCENE 2: Connected Digital Hieroglyphic Network Lines
            if (currentScene == IntroScene.SCENE_2_SYMBOLS || currentScene == IntroScene.SCENE_5_PORTAL_OPEN) {
                val netRadius = min(canvasW, canvasH) * 0.36f
                val points = glyphs.map { g ->
                    val rad = Math.toRadians(g.angle.toDouble() + (celestialRotation * 0.15))
                    Offset(
                        center.x + (netRadius * cos(rad)).toFloat(),
                        center.y + (netRadius * sin(rad)).toFloat()
                    )
                }

                // Connect adjacent symbols with glowing light streams
                for (i in points.indices) {
                    val next = points[(i + 1) % points.size]
                    drawLine(
                        color = HorusCyan.copy(alpha = 0.35f * energyPulse),
                        start = points[i],
                        end = next,
                        strokeWidth = 1.8f
                    )
                    // Connect across to create web
                    val across = points[(i + 2) % points.size]
                    drawLine(
                        color = HorusGold.copy(alpha = 0.2f),
                        start = points[i],
                        end = across,
                        strokeWidth = 1f
                    )
                }
            }

            // SCENE 3 & 5: Energy shockwave pulse
            if (currentScene == IntroScene.SCENE_3_EYE_OF_HORUS || currentScene == IntroScene.SCENE_5_PORTAL_OPEN) {
                val waveRadius = min(canvasW, canvasH) * (0.15f + 0.35f * energyPulse)
                drawCircle(
                    color = HorusCyan.copy(alpha = (1f - energyPulse) * 0.5f),
                    radius = waveRadius,
                    center = center,
                    style = Stroke(width = 3f)
                )
            }
        }

        // LAYER 2: SCENE CONTENT (Animated States & Cinematic Typography)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            when (currentScene) {
                IntroScene.SCENE_1_VOID -> {
                    // Scene 1: Mysterious Void awakening
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Text(
                            text = "☥",
                            fontSize = 42.sp,
                            color = HorusGold.copy(alpha = energyPulse),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = if (isArabic) "في البدء… كانت الرموز تحرس المعرفة" else "In the beginning… symbols guarded wisdom",
                            color = HorusCyan.copy(alpha = 0.85f),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            letterSpacing = 1.sp
                        )
                    }
                }

                IntroScene.SCENE_2_SYMBOLS -> {
                    // Scene 2: Hieroglyphic symbols awaken & float
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val introSymbols = listOf("☥", "𓋹", "𓂀", "𓆣", "𓏛", "𓋖", "𓊗")
                            introSymbols.forEachIndexed { idx, symbol ->
                                val delayAlpha by animateFloatAsState(
                                    targetValue = 1f,
                                    animationSpec = tween(600, delayMillis = idx * 120),
                                    label = "symbol_fade_$idx"
                                )
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(HorusCardNavy.copy(alpha = 0.75f))
                                        .border(1.5.dp, HorusGold.copy(alpha = 0.6f * energyPulse), RoundedCornerShape(8.dp))
                                        .alpha(delayAlpha),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = symbol,
                                        fontSize = 19.sp,
                                        color = if (idx % 2 == 0) HorusGold else HorusCyan,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(28.dp))

                        Text(
                            text = if (isArabic) "تستيقظ الرموز القديمة… وتتحول إلى شيفرات حية" else "Ancient symbols awaken… transforming into living code",
                            color = HorusLightGold,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = if (isArabic) "لغة برمجية جديدة تنبثق من أعماق التاريخ" else "A new programming language emerges from the depths of history",
                            color = HorusMutedIvory,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                IntroScene.SCENE_3_EYE_OF_HORUS -> {
                    // Scene 3: The Climax - Eye of Horus Awakening
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // Radiant Eye of Horus Emblem with glowing layers
                        Box(
                            modifier = Modifier
                                .size(130.dp)
                                .scale(0.9f + (energyPulse * 0.15f))
                                .clip(CircleShape)
                                .background(
                                    Brush.radialGradient(
                                        listOf(
                                            HorusCyan.copy(alpha = 0.35f),
                                            HorusMidnight.copy(alpha = 0.85f),
                                            Color.Transparent
                                        )
                                    )
                                )
                                .border(2.5.dp, HorusGold, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "𓂀",
                                fontSize = 72.sp,
                                color = HorusGold,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = if (isArabic) "عين حورس: بؤرة البصيرة والمنطق" else "The Eye of Horus: Beacon of Insight & Logic",
                            color = HorusGold,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = if (isArabic) "ميزان الحكمة يزن الأوامر والشروط بدقة مطلقة" else "The balance of wisdom evaluates instructions and decisions with absolute precision",
                            color = HorusElectricCyan,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                IntroScene.SCENE_4_TITLE_REVEAL, IntroScene.SCENE_5_PORTAL_OPEN -> {
                    // Scene 4 & 5: Grand Title & Slogan Reveal + Portal Opening
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        // Eye of Horus emblem above title
                        Box(
                            modifier = Modifier
                                .size(76.dp)
                                .clip(CircleShape)
                                .background(HorusCardNavy.copy(alpha = 0.8f))
                                .border(1.5.dp, HorusGold, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("𓂀", fontSize = 42.sp, color = HorusGold, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // English Title Reveal
                        Text(
                            text = "HORUS CODE",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 4.sp,
                                fontSize = 34.sp
                            ),
                            color = HorusGold,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Arabic Title Reveal
                        Text(
                            text = "شِفرة حورس",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 28.sp
                            ),
                            color = HorusCyan,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Slogan Card
                        Card(
                            modifier = Modifier
                                .fillMaxWidth(0.92f)
                                .clip(RoundedCornerShape(12.dp)),
                            colors = CardDefaults.cardColors(containerColor = HorusMidnight.copy(alpha = 0.85f)),
                            border = CardDefaults.outlinedCardBorder().copy(brush = SolidColor(HorusSubtleBorder.copy(alpha = 0.7f)))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "«من رموز الماضي… إلى شيفرة المستقبل.»",
                                    color = HorusIvory,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "“From the symbols of the past to the code of the future.”",
                                    color = HorusLightGold,
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        if (currentScene == IntroScene.SCENE_5_PORTAL_OPEN) {
                            Spacer(modifier = Modifier.height(20.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = HorusCyan,
                                    strokeWidth = 2.dp
                                )
                                Text(
                                    text = if (isArabic) "يفتح المعبد بوابته الرقمية…" else "The digital portal awakens…",
                                    color = HorusCyan,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                IntroScene.SCENE_6_WELCOME -> {
                    // Scene 6: Welcome Screen & Call to Action
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(0.95f)
                            .testTag("intro_welcome_card"),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = HorusMidnight.copy(alpha = 0.95f)),
                        border = CardDefaults.outlinedCardBorder().copy(brush = SolidColor(HorusGold.copy(alpha = 0.75f)))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Emblem
                            Box(
                                modifier = Modifier
                                    .size(70.dp)
                                    .clip(CircleShape)
                                    .background(HorusCardNavy)
                                    .border(2.dp, HorusGold, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("𓂀", fontSize = 38.sp, color = HorusGold, fontWeight = FontWeight.Bold)
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = "HORUS CODE | شِفرة حورس",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = HorusIvory,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = if (isArabic) "«من رموز الماضي… إلى شيفرة المستقبل»" else "“From past symbols to future code”",
                                color = HorusCyan,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "Created by Amr Ahmed | عمرو أحمد",
                                color = HorusLightGold,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            // PRIMARY CTA BUTTON: BEGIN YOUR JOURNEY
                            Button(
                                onClick = onFinish,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp)
                                    .testTag("begin_journey_button"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = HorusCyan,
                                    contentColor = HorusDeepNavy
                                ),
                                shape = RoundedCornerShape(12.dp),
                                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = if (isArabic) "ابدأ الرحلة | BEGIN YOUR JOURNEY" else "BEGIN YOUR JOURNEY | ابدأ الرحلة",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // LAYER 3: TOP BAR OVERLAY (Skip button, Language toggle, Sound/Haptics toggle)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Language & Sound quick toggles
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Language Switcher
                OutlinedButton(
                    onClick = onToggleLanguage,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = HorusIvory),
                    border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(brush = SolidColor(HorusSubtleBorder.copy(alpha = 0.6f))),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.testTag("intro_lang_toggle")
                ) {
                    Text(
                        text = if (isArabic) "EN" else "عربي",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = HorusGold
                    )
                }

                // Haptics/Audio Toggle
                IconButton(
                    onClick = { isHapticsEnabled = !isHapticsEnabled },
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(HorusCardNavy.copy(alpha = 0.6f))
                        .testTag("intro_haptics_toggle")
                ) {
                    Icon(
                        imageVector = if (isHapticsEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                        contentDescription = "Haptics",
                        tint = if (isHapticsEnabled) HorusCyan else HorusMutedIvory.copy(alpha = 0.5f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // DISCREET SKIP INTRO BUTTON (Always functional at any moment!)
            TextButton(
                onClick = onSkip,
                colors = ButtonDefaults.textButtonColors(contentColor = HorusMutedIvory),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(HorusMidnight.copy(alpha = 0.5f))
                    .testTag("skip_intro_button")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = if (isArabic) "تخطي المقدمة" else "Skip Intro",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = HorusMutedIvory
                    )
                    Icon(
                        imageVector = Icons.Default.FastForward,
                        contentDescription = "Skip",
                        tint = HorusMutedIvory,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }

        // LAYER 4: BOTTOM TIMELINE SCENE SCRUBBER (Allows judges to jump to any scene)
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IntroScene.values().forEach { scn ->
                val isCurrent = currentScene == scn
                Box(
                    modifier = Modifier
                        .size(if (isCurrent) 10.dp else 6.dp)
                        .clip(CircleShape)
                        .background(
                            if (isCurrent) HorusGold else HorusSubtleBorder.copy(alpha = 0.5f)
                        )
                        .clickable { currentScene = scn }
                        .testTag("scene_dot_${scn.name}")
                )
            }
        }
    }
}

/**
 * Draws an ancient monumental Egyptian pylon gateway with geometric architectural lines.
 */
private fun DrawScope.drawTempleDoorway(center: Offset, canvasW: Float, canvasH: Float, alpha: Float) {
    if (alpha <= 0.05f) return

    val doorW = canvasW * 0.72f
    val doorH = canvasH * 0.55f
    val doorLeft = center.x - (doorW / 2f)
    val doorTop = center.y - (doorH * 0.45f)
    val doorRight = doorLeft + doorW

    val strokeCol = HorusCyan.copy(alpha = alpha * 0.45f)
    val accentGold = HorusGold.copy(alpha = alpha * 0.6f)

    // Left Monumental Pylon Column
    val pylonW = doorW * 0.22f
    drawRect(
        color = Color(0xFF061028).copy(alpha = alpha * 0.35f),
        topLeft = Offset(doorLeft, doorTop + 20f),
        size = Size(pylonW, doorH - 20f)
    )
    drawRect(
        color = strokeCol,
        topLeft = Offset(doorLeft, doorTop + 20f),
        size = Size(pylonW, doorH - 20f),
        style = Stroke(width = 1.2f)
    )

    // Right Monumental Pylon Column
    drawRect(
        color = Color(0xFF061028).copy(alpha = alpha * 0.35f),
        topLeft = Offset(doorRight - pylonW, doorTop + 20f),
        size = Size(pylonW, doorH - 20f)
    )
    drawRect(
        color = strokeCol,
        topLeft = Offset(doorRight - pylonW, doorTop + 20f),
        size = Size(pylonW, doorH - 20f),
        style = Stroke(width = 1.2f)
    )

    // Top Cornice / Architectural Arch Trave
    val corniceTop = doorTop - 15f
    val cornicePath = Path().apply {
        moveTo(doorLeft - 20f, doorTop + 20f)
        lineTo(doorRight + 20f, doorTop + 20f)
        lineTo(doorRight + 40f, corniceTop)
        lineTo(doorLeft - 40f, corniceTop)
        close()
    }
    drawPath(cornicePath, Color(0xFF061028).copy(alpha = alpha * 0.5f))
    drawPath(cornicePath, accentGold, style = Stroke(width = 1.5f))

    // Inner Gateway Aperture (Portal Entry)
    val innerLeft = doorLeft + pylonW + 15f
    val innerRight = doorRight - pylonW - 15f
    val innerW = innerRight - innerLeft

    drawRect(
        color = strokeCol,
        topLeft = Offset(innerLeft, doorTop + 35f),
        size = Size(innerW, doorH - 35f),
        style = Stroke(width = 1.5f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f), 0f))
    )

    // Glowing Sun Disc on the Cornice
    val discCenter = Offset(center.x, corniceTop + 10f)
    drawCircle(
        color = HorusGold.copy(alpha = alpha * 0.8f),
        radius = 16f,
        center = discCenter
    )
    // Winged Sun extensions
    drawLine(
        color = accentGold,
        start = Offset(discCenter.x - 50f, discCenter.y),
        end = Offset(discCenter.x + 50f, discCenter.y),
        strokeWidth = 2f
    )
}

/**
 * Triggers subtle haptic feedback for scene transitions.
 */
private fun triggerHapticFeedback(context: Context, scene: IntroScene) {
    try {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator ?: return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val duration = when (scene) {
                IntroScene.SCENE_3_EYE_OF_HORUS -> 60L
                IntroScene.SCENE_4_TITLE_REVEAL -> 45L
                IntroScene.SCENE_5_PORTAL_OPEN -> 35L
                else -> 20L
            }
            vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(30L)
        }
    } catch (_: Exception) {
        // Graceful fallback if vibrator is unavailable
    }
}
