<template>
  <v-container fluid id="googleCloudExam">
    <v-card>
      <div style="padding: 10px">This is under construction</div>
    </v-card>

  </v-container>
</template>

<script>
import QuestionService from "../services/QuestionService";

export default {
  name: "GoogleCloudExam",
  components: {
  },
  data: () => ({
    issues: {},
    loading: true,
    editedItem: {},
    selected: [],
    dialog: false,
    filter: "",
    searchString: "",
    filterError: false,
    rules: [
      () => (!this.filterError) || "There's an error with your filter"
    ]
  }),
  computed: {
    hint: function() {
      let hint = "";
      if (this.issues.headers !== undefined) {
        let fields = this.issues.headers.map(header => header.text).join(", ");
        hint = "Possible fields are: ${fields}.".replace("${fields}", fields);
      }
      return hint;
    },
    errorMessage: function () {
      let errorMessage = ""
      if (this.filterError) {
        errorMessage = "There's an error with your filter";
      }
      return errorMessage;
    }
  },
  mounted: function () {
    this.getQuestions();
  },
  methods: {
    /**
     * Gets all the issues from the database.
     */
    async getQuestions() {
      const {data} = await QuestionService.getQuestions();
      this.issues = data;
      this.loading = false;
      console.log(this.issues)
    },

    /**
     * Cancels item's edition.
     */
    cancel() {
      this.dialog = false;
      this.cleanEditedItem();
    },

    /**
     * Cleans the edited item.
     */
    cleanEditedItem() {
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, {});
      });
    },

    /**
     * Sets the filter based on the current event.
     * If the event is a "KeyboardEvent" (on Enter key pressed), then the filter is set to the current string;
     * If the event is a "MouseEvent", or something else (like a click on the clear button inside the textfield),
     *  then the filter is set to an empty string.
     *
     * @param event the event to be checked
     */
    setFilter(event) {
      if (event instanceof KeyboardEvent) {
        this.searchString = this.filter;
      } else {
        this.searchString = "";
      }
      this.filterError = false;
    },
  }

}
</script>

<style scoped>

</style>