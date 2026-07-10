package com.example.ebook.data

import androidx.compose.ui.geometry.Offset
import com.example.ebook.R
import com.example.ebook.model.Book
import com.example.ebook.model.BookCovers
import com.example.ebook.model.ShelfBook
import com.example.ebook.model.SpiralTrajectory
object BookCatalog {
    val featuredCovers = BookCovers(
        front = R.drawable.front_cover,
        back = R.drawable.back_cover,
        spine = R.drawable.spine_cover,
    )
    val fullCoverBooks: List<BookCovers> = listOf(
        BookCovers(
            front = R.drawable.ganbatte_front_cover,
            back = R.drawable.ganbatte_back_cover,
            spine = R.drawable.ganbatte_spine_cover,
        ),
        BookCovers(
            front = R.drawable.kitchen_front_cover,
            back = R.drawable.kitchen_back_cover,
            spine = R.drawable.kitchen_spine_cover,
        ),
        BookCovers(
            front = R.drawable.las_munecas_chinas_front_cover,
            back = R.drawable.las_munecas_chinas_back_cover,
            spine = R.drawable.las_munecas_chinas_spine_cover,
        ),
        BookCovers(
            front = R.drawable.los_lobos_cambian_el_rio_front_cover,
            back = R.drawable.los_lobos_cambian_el_rio_back_cover,
            spine = R.drawable.los_lobos_cambian_el_rio_spine_cover,
        ),
        BookCovers(
            front = R.drawable.die_and_retry_front_cover,
            back = R.drawable.die_and_retry_back_cover,
            spine = R.drawable.die_and_retry_spine_cover,
        ),
    )

    private val column1 = listOf(
        ShelfBook(
            Book(
                title = "Front",
                frontCover = R.drawable.front_cover,
                backCover = R.drawable.back_cover,
                spineCover = R.drawable.spine_cover,
            ),
        ),
        ShelfBook(Book("The Law of Human Nature", R.drawable.the_law_of_human_nature_front_cover)),
        ShelfBook(Book("The Mountain Is You", R.drawable.the_mountain_is_you_front_cover)),
        ShelfBook(
            Book("The Picture of Dorian Gray", R.drawable.the_picture_of_dorian_gray_front_cover),
        ),
        ShelfBook(Book("Without a Trace", R.drawable.without_a_trace_front_cover)),
        ShelfBook(
            Book("Think and Grow Rich", R.drawable.think_and_grow_rich_front_cover),
            trajectory = SpiralTrajectory(
                destination = Offset(-1200f, 1000f),
                control1 = Offset(2500f, -100f),
                control2 = Offset(2500f, 3000f),
            ),
            slot = 1,
        ),
        ShelfBook(
            Book(
                title = "Ganbatte!",
                frontCover = R.drawable.ganbatte_front_cover,
                backCover = R.drawable.ganbatte_back_cover,
                spineCover = R.drawable.ganbatte_spine_cover,
            ),
            trajectory = SpiralTrajectory(
                destination = Offset(-1200f, 1050f),
                control1 = Offset(2500f, -400f),
                control2 = Offset(2500f, 3000f),
            ),
            slot = 2,
        ),
        ShelfBook(
            Book(
                title = "Kitchen",
                frontCover = R.drawable.kitchen_front_cover,
                backCover = R.drawable.kitchen_back_cover,
                spineCover = R.drawable.kitchen_spine_cover,
            ),
            trajectory = SpiralTrajectory(
                destination = Offset(2000f, 1100f),
                control1 = Offset(-1500f, -400f),
                control2 = Offset(2500f, -400f),
            ),
            slot = 3,
        ),
        ShelfBook(
            Book(
                title = "Las Muñecas Chinas",
                frontCover = R.drawable.las_munecas_chinas_front_cover,
                backCover = R.drawable.las_munecas_chinas_back_cover,
                spineCover = R.drawable.las_munecas_chinas_spine_cover,
            ),
            trajectory = SpiralTrajectory(
                destination = Offset(2000f, 1100f),
                control1 = Offset(-1500f, -400f),
                control2 = Offset(2500f, -400f),
            ),
            slot = 4,
        ),
        ShelfBook(
            Book(
                title = "Los Lobos Cambian el Río",
                frontCover = R.drawable.los_lobos_cambian_el_rio_front_cover,
                backCover = R.drawable.los_lobos_cambian_el_rio_back_cover,
                spineCover = R.drawable.los_lobos_cambian_el_rio_spine_cover,
            ),
            trajectory = SpiralTrajectory(
                destination = Offset(2000f, 1100f),
                control1 = Offset(-1500f, -400f),
                control2 = Offset(2500f, -400f),
            ),
            slot = 5,
        ),
        ShelfBook(
            Book(
                title = "Die and Retry",
                frontCover = R.drawable.die_and_retry_front_cover,
                backCover = R.drawable.die_and_retry_back_cover,
                spineCover = R.drawable.die_and_retry_spine_cover,
            ),
            trajectory = SpiralTrajectory(
                destination = Offset(2000f, 1100f),
                control1 = Offset(-1500f, -400f),
                control2 = Offset(2500f, -400f),
            ),
            slot = 6,
        ),
    )

    private val column2 = listOf(
        ShelfBook(
            Book("Smashed Idols", R.drawable.smashed_idols),
            trajectory = SpiralTrajectory(
                destination = Offset(-1500f, 1050f),
                control1 = Offset(2000f, -1000f),
                control2 = Offset(2500f, 3000f),
            ),
            slot = 6,
        ),
        ShelfBook(
            Book("What Remains of Her", R.drawable.what_remains_of_her),
            trajectory = SpiralTrajectory(
                destination = Offset(-1500f, 1050f),
                control1 = Offset(2500f, -1000f),
                control2 = Offset(2500f, 3000f),
            ),
            slot = 7,
        ),
        ShelfBook(Book("On the Run", R.drawable.on_the_run)),
        ShelfBook(
            Book("Things I Never Got to Tell You", R.drawable.things_i_never_got_to_tell_you),
        ),
        ShelfBook(Book("Alfred Saves the Day", R.drawable.alfred_saves_the_day)),
        ShelfBook(
            Book("Zodak the Last Shielder", R.drawable.zodak_the_last_shielder),
            trajectory = SpiralTrajectory(
                destination = Offset(-1500f, 1000f),
                control1 = Offset(2500f, -100f),
                control2 = Offset(2500f, 3000f),
            ),
        ),
        ShelfBook(
            Book("Lonely Space", R.drawable.lonely_space),
            trajectory = SpiralTrajectory(
                destination = Offset(-1500f, 1050f),
                control1 = Offset(2500f, -400f),
                control2 = Offset(2500f, 3000f),
            ),
        ),
        ShelfBook(
            Book(
                title = "The Dazzling Magic of a Petit Four",
                frontCover = R.drawable.the_dazzling_magic_of_a_petit_four,
            ),
        ),
        ShelfBook(
            Book("The Death and Life of Amy Smith", R.drawable.the_death_and_life_of_amy_smith),
            trajectory = SpiralTrajectory(
                destination = Offset(2000f, 1100f),
                control1 = Offset(-2500f, -400f),
                control2 = Offset(2500f, -400f),
            ),
            slot = 2,
        ),
        ShelfBook(
            Book("Nelly Finds Her Niche", R.drawable.nelly_finds_her_niche),
            trajectory = SpiralTrajectory(
                destination = Offset(2000f, 1200f),
                control1 = Offset(-2500f, 100f),
                control2 = Offset(2500f, -400f),
            ),
            slot = 1,
        ),
        ShelfBook(
            Book("I Will Watch from Up Here", R.drawable.i_will_watch_from_up_here),
            trajectory = SpiralTrajectory(
                destination = Offset(2000f, 1100f),
                control1 = Offset(-2000f, 1500f),
                control2 = Offset(2500f, -2000f),
            ),
            slot = 3,
        ),
    )

    private val column3 = listOf(
        ShelfBook(
            Book("Farewell to Summer", R.drawable.farewell_to_summer),
            slot = 4,
        ),
        ShelfBook(
            Book("In the Footsteps of Baal", R.drawable.in_the_footsteps_of_baal),
            slot = 5,
        ),
        ShelfBook(Book("The Book of Jacob", R.drawable.the_book_of_jacob)),
        ShelfBook(
            Book("The Alchemy of Emotions", R.drawable.the_alchemy_of_emotions),
            trajectory = SpiralTrajectory(
                destination = Offset(-1500f, 1050f),
                control1 = Offset(2500f, -1000f),
                control2 = Offset(2500f, 3000f),
            ),
            slot = 6,
        ),
        ShelfBook(
            Book("After the Peter", R.drawable.after_the_peter),
            trajectory = SpiralTrajectory(
                destination = Offset(-1500f, 1050f),
                control1 = Offset(2500f, -1000f),
                control2 = Offset(2500f, 3000f),
            ),
            slot = 7,
        ),
        ShelfBook(
            Book("A Siren Song", R.drawable.a_siren_song),
            trajectory = SpiralTrajectory(
                destination = Offset(-1200f, 1000f),
                control1 = Offset(2500f, -100f),
                control2 = Offset(2500f, 3000f),
            ),
        ),
        ShelfBook(
            Book("Entranced", R.drawable.entranced),
            trajectory = SpiralTrajectory(
                destination = Offset(2000f, 1200f),
                control1 = Offset(-2500f, 1500f),
                control2 = Offset(2000f, -1000f),
            ),
            slot = 3,
        ),
        ShelfBook(
            Book("Converse", R.drawable.converse),
            trajectory = SpiralTrajectory(
                destination = Offset(2000f, 1200f),
                control1 = Offset(-2500f, 1500f),
                control2 = Offset(2000f, -1000f),
            ),
            slot = 4,
        ),
        ShelfBook(
            Book("Pearl Bound", R.drawable.pearl_bound),
            slot = 2,
        ),
        ShelfBook(
            Book(
                title = "Crossing the Sea of Shattered Glass",
                frontCover = R.drawable.crossing_the_sea_of_shattered_glass,
            ),
            slot = 1,
        ),
        ShelfBook(
            Book("American Apostasy", R.drawable.american_apostasy),
            trajectory = SpiralTrajectory(
                destination = Offset(2000f, 1100f),
                control1 = Offset(-2000f, 1500f),
                control2 = Offset(2500f, -2000f),
            ),
            slot = 3,
        ),
    )

    private val column4 = listOf(
        ShelfBook(
            Book("Mist", R.drawable.mist),
            trajectory = SpiralTrajectory(
                destination = Offset(-1500f, 1050f),
                control1 = Offset(2500f, -1000f),
                control2 = Offset(2500f, 3000f),
            ),
            slot = 6,
        ),
        ShelfBook(
            Book("Before Tomorrow", R.drawable.before_tomorrow),
            trajectory = SpiralTrajectory(
                destination = Offset(-1500f, 1050f),
                control1 = Offset(2500f, -1000f),
                control2 = Offset(2500f, 3000f),
            ),
            slot = 5,
        ),
        ShelfBook(Book("An Anthropology of Wandering", R.drawable.an_anthropology_of_wandering)),
        ShelfBook(
            Book("And Never Memeory of You", R.drawable.and_never_memeory_of_you),
            trajectory = SpiralTrajectory(
                destination = Offset(-1500f, 1050f),
                control1 = Offset(2500f, -1000f),
                control2 = Offset(2500f, 3000f),
            ),
            slot = 6,
        ),
        ShelfBook(
            Book("Tom Ryan's Shows", R.drawable.tom_ryans_shows),
            trajectory = SpiralTrajectory(
                destination = Offset(-1500f, 1050f),
                control1 = Offset(2500f, -1000f),
                control2 = Offset(2500f, 3000f),
            ),
            slot = 7,
        ),
        ShelfBook(
            Book("What Keeps Us", R.drawable.what_keeps_us),
            trajectory = SpiralTrajectory(
                destination = Offset(-1200f, 1000f),
                control1 = Offset(2500f, -100f),
                control2 = Offset(2500f, 3000f),
            ),
        ),
        ShelfBook(
            Book("Flight of the Hummingbird", R.drawable.flight_of_the_hummingbird),
            trajectory = SpiralTrajectory(
                destination = Offset(2000f, 1200f),
                control1 = Offset(-2500f, 1500f),
                control2 = Offset(2000f, -1000f),
            ),
            slot = 3,
        ),
        ShelfBook(
            Book("The Wilusiad", R.drawable.the_wilusiad),
            trajectory = SpiralTrajectory(
                destination = Offset(2000f, 1200f),
                control1 = Offset(-2500f, 1500f),
                control2 = Offset(2000f, -1000f),
            ),
            slot = 4,
        ),
        ShelfBook(
            Book("Meat", R.drawable.meat),
            slot = 2,
        ),
        ShelfBook(
            Book("Faith", R.drawable.faith),
            slot = 1,
        ),
        ShelfBook(
            Book("The Mermaid's Wrath", R.drawable.the_mermaids_wrath),
            trajectory = SpiralTrajectory(
                destination = Offset(-1500f, 1050f),
                control1 = Offset(2500f, -1000f),
                control2 = Offset(2500f, 3000f),
            ),
            slot = 8,
        ),
    )

    val columns: List<List<ShelfBook>> = listOf(column1, column2, column3, column4)
}
