
import com.searchbuddy.searchbuddy.model.Edu.Board

data class UpdateEducationDetailRequest(
    val id: Int,
    val professionalDetails: Any
)
data class ProfessionalDetail(
    val educationDetails: Any
)
data class EducationDetail(
    val graduation: Any,
    val intermediate: Any,
    val metric: Any,
    val postGraduation: Any
)
data class Metri(
    val board: Board,
    val passingYear: Int,
    val percentage: String,
    val school: String,
    val startYear: Int
)
data class PostGraduatio(
    val degree: String,
    val passingYear: Any,
    val percentage: Any,
    val specialization: Any,
    val startYear: Any,
    val university: String
)
data class Intermediat(
    val board: Board,
    val passingYear: Int,
    val percentage: String,
    val school: String,
    val startYear: Int
)