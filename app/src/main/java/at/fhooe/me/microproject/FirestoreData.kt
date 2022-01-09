package at.fhooe.me.microproject

data class FirestoreData(
    public val firstName: String,
    public val priorityA: ArrayList<String>,
    public val priorityB: ArrayList<String>,
    public val priorityC: ArrayList<String>,
    public val priorityD: ArrayList<String>
)
